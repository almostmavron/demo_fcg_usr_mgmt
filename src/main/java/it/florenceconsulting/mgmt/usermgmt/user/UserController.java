package it.florenceconsulting.mgmt.usermgmt.user;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
     * @return a list of users, possibly empty
     */
    @GetMapping("/search")
    public List<User> getUsers(
            @RequestParam(required = false) Long id, 
            @RequestParam(required = false) String lastname,
            @RequestParam(required = false) String firstname ) {
        
        ArrayList<User> list = new ArrayList<User>();
        if (id != null) {
            try {
                list.add(getUserById(id));                
            } catch (Exception e) {
                // we just leave the list empty
            }
        } else if (hasText(lastname) || hasText(firstname)) {
            try {
                // return userRepo.findByFirstname(firstname);
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
        } catch (Exception e) {
            return "User not found";
        }
    }
}
