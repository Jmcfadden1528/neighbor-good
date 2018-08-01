package org.launchcode.capstoneprojectjm.controllers;


import org.launchcode.capstoneprojectjm.models.Data.EventDao;
import org.launchcode.capstoneprojectjm.models.Data.UserDao;

import org.launchcode.capstoneprojectjm.models.Event;
import org.launchcode.capstoneprojectjm.models.User;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;


@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private EventDao eventDao; // Allows us to interact with database


    @Autowired
    private UserDao userDao;


    //    TODO: display in chronological order
    @RequestMapping(value = "edit-events")
    public String adminConsole(Model model, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }
        User user = userDao.findByUsername(username).get(0);
        if (user.getAdmin() != true) {
            return "redirect:/user/access-denied";
        }

        Iterable<Event> events = eventDao.findAll();


        model.addAttribute("events", events);
        model.addAttribute("title", "All Events");
        model.addAttribute("currentUser", user);
        return "admin/edit-events";
    }


    @RequestMapping(value = "remove-event", method = RequestMethod.POST)
    public String processRemoveEventAdmin(Model model, @CookieValue(value = "user", defaultValue = "none") String username, @RequestParam int[] ids) {
        User user = userDao.findByUsername(username).get(0);

        if (username.equals("none")) {
            return "redirect:user/login";
        }

        User u = userDao.findByUsername(username).get(0);
        List<Event> userEventsEdit = u.getEvents();
        for (int id : ids) {
            userEventsEdit.remove(eventDao.findOne(id));
        }

        u.setEvents(userEventsEdit);
        userDao.save(u);
        for (int id : ids) {
            Event event = eventDao.findOne(id);
            System.out.println(event.getUsers());
            List theUsers = event.getUsers();
            event.clearUsers(theUsers);
            userDao.save(theUsers);
            eventDao.save(event);
            System.out.println(event.getUsers());
            eventDao.delete(id);

        }

        model.addAttribute("currentUser", user);
        model.addAttribute("title", "Not sure");
        Iterable<Event> events = eventDao.findAll();
        model.addAttribute("events", events);

        return "admin/edit-events";
    }

    @RequestMapping(value = "edit-users")
    public String displayUsersEditForm(Model model, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }
        User user = userDao.findByUsername(username).get(0);
        if (user.getAdmin() != true) {
            return "redirect: /user/access-denied";
        }

        List<User> users = new ArrayList<>();
        for (User thisUser : userDao.findAll()) {
            if (thisUser.getAdmin() != true) {
                users.add(thisUser);
                userDao.save(thisUser);
            }

        }
        List<User> adminUsers = new ArrayList<>();
        for (User thisUser : userDao.findAll()) {
            System.out.println(thisUser.getAdmin());
            if (thisUser.getAdmin() == true) {
                adminUsers.add(thisUser);
            }
            if (user.getAdmin() != true) {
                continue;
            }
        }


        model.addAttribute("users", users);
        model.addAttribute("adminUsers", adminUsers);
        model.addAttribute("title", "Edit Users");
        model.addAttribute("currentUser", user);
        return "admin/edit-users";
    }

    @RequestMapping(value = "add-admin", method = RequestMethod.POST)
    public String processAddAdminForm(Model model, @CookieValue(value = "user", defaultValue = "none") String username, @RequestParam int[] ids) {


        if (username.equals("none")) {
            return "redirect:user/login";
        }
        for (int id : ids) {
            User user = userDao.findOne(id);
            user.setAdmin(true);
            userDao.save(user);
        }
        model.addAttribute("currentUser", userDao.findByUsername(username).get(0));
        return "redirect:/admin/edit-users";
    }

    @RequestMapping(value = "remove-admin", method = RequestMethod.POST)
    public String processRemoveAdminForm(Model model, @CookieValue(value = "user", defaultValue = "none") String username, @RequestParam int[] ids) {


        if (username.equals("none")) {
            return "redirect:user/login";
        }
        for (int id : ids) {
            User user = userDao.findOne(id);
            user.setAdmin(false);
            userDao.save(user);
        }
        model.addAttribute("currentUser", userDao.findByUsername(username).get(0));
        return "redirect:/admin/edit-users";
    }

}
