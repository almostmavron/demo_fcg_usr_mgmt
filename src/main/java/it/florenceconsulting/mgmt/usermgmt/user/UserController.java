package it.florenceconsulting.mgmt.usermgmt.user;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import static org.springframework.util.StringUtils.hasText;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    protected UserRepo userRepo;

    /**
     * Return a list of users matching the provided search parameters.
     * At least one search parameter must be provided.
     * 
     * @param id - user's id
     * @param firstname - user's first name
     * @param laststname - user's last name
     * @param email - user's email
     * @return a list of users, possibly empty
     */
    @GetMapping("/search")
    public List<User> getUsers(
            @RequestParam(required = false) Long id, 
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String firstname,
            @RequestParam(required = false) String email ) {
        
        List<User> list = new ArrayList<User>();

        if (id != null) {
            try {
                list.add(getUserById(id));                
            } catch (Exception e) {
                // we just leave the list empty
            }
        } else if (hasText(email)) {
            try {
                list = userRepo.findByEmail(email);
            } catch (Exception e) {
                 // we just leave the list empty
            }
        } else if (hasText(lastname) || hasText(firstname)) {
            try {
                list = userRepo.findByNames(lastname, firstname);
            } catch (Exception e) {
                // we just leave the list empty
            }
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You need to provide at least one search parameter");
        }
        
        return list;
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        try {
            return userRepo.findById(id).get();
        } catch (java.util.NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepo.save(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            User existingUser = userRepo.findById(id).get();
            existingUser.setFirstname(user.getFirstname());
            existingUser.setLastname(user.getLastname());
            existingUser.setEmail(user.getEmail());
            existingUser.setAddress(user.getAddress());
    
            return userRepo.save(existingUser);
        } catch (java.util.NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        try {
            // deleteById silently ignores deletion of non-existant entities
            // so we fetch it first
            userRepo.findById(id).get();
            userRepo.deleteById(id);
            return "User deleted successfully";
        } catch (java.util.NoSuchElementException e) {
            return "User not found";
        }
    }

    @PostMapping("/upload-csv")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String upLoadCSV(@RequestParam("file") MultipartFile file) throws IOException {
        try {
            
            CSVFormat cvsFormat = CSVFormat.DEFAULT.withHeader("firstname", "lastname", "email", "address")
            .withIgnoreEmptyLines().withTrim().withIgnoreSurroundingSpaces();
            
            // using parse instead of constructor in order to read one record at the time in memory
            // TODO check if this is enough, because MultipartFile stream is already buffered 
            // (eg because of http "chunked") or if we need to wrap the InputStream  in a BufferedReader
            CSVParser parser = CSVParser.parse(file.getInputStream(), Charset.defaultCharset(), cvsFormat);
            
            for (CSVRecord csvRecord : parser.getRecords()) {
                User user = new User();
                user.setFirstname(csvRecord.get("firstname"));
                user.setLastname(csvRecord.get("lastname"));
                user.setEmail(csvRecord.get("email"));
                user.setAddress(csvRecord.get("address"));
                
                System.out.println("Loading user: " + user);

                userRepo.saveAndFlush(user);
                /*  TODO use a RestTemplate to create a user via http call, asynchronously,
                    in order to process a certain number of csv rows in parallel.
                    @see MvcUriComponentsBuilder (for the service url)
                    @see @Async, RestTemplate.postForLocation
                    @see taskExecutor method (for the thread pool)
                    @see CompletableFuture in order to join them and know when you're done
                    Push futures in a queue and start joining them as soon as you reached a predetermind threshold                    
                */
            }           
            
            // userRepo.flush();
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        return "File acquired (" + file.getOriginalFilename() + ")";
    }
}
