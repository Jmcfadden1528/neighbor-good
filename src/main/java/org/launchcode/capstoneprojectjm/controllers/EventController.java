package org.launchcode.capstoneprojectjm.controllers;

import org.apache.tomcat.jni.Local;
import org.launchcode.capstoneprojectjm.models.Data.AddressDao;
import org.launchcode.capstoneprojectjm.models.Data.EventDao;
import org.launchcode.capstoneprojectjm.models.Data.UserDao;
import org.launchcode.capstoneprojectjm.models.Event;
import org.launchcode.capstoneprojectjm.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;



@Controller
@RequestMapping("event")
public class EventController {


    @Autowired
    private EventDao eventDao; // Allows us to interact with database

    @Autowired
    private AddressDao addressDao;

    @Autowired
    private UserDao userDao;

    //    TODO: display in chronological order
    @RequestMapping(value = "")
    public String index(Model model, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }


        Iterable<Event> events = eventDao.findAll();
        List<User> u = userDao.findByUsername(username);
        User currentUser = u.get(0);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("events", events);
        model.addAttribute("title", "All Events");

        return "event/index";
    }


    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String displayAddEventForm(Model model, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }
        List<User> u = userDao.findByUsername(username);
        User currentUser = u.get(0);
        if (currentUser.getAdmin() != true) {
            return "redirect:/user/access-denied";

        }
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("title", "Add Event");
        model.addAttribute(new Event());
        return "event/add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String processAddEventForm(@ModelAttribute @Valid Event newEvent,
                                      Errors errors, Model model, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }
        if (errors.hasErrors()) {
            List<User> u = userDao.findByUsername(username);
            User currentUser = u.get(0);
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("title", "Add Event");
            model.addAttribute("event", newEvent);
            return "event/add";
        }

        User u = userDao.findByUsername(username).get(0);
        eventDao.save(newEvent);
        model.addAttribute("currentUser", u);

        return "event/event-added";
    }



    @RequestMapping(value = "event-view/{id}")
    public String displayEvent(Model model, @PathVariable int id, @CookieValue(value = "user", defaultValue = "none") String username){

        if (username.equals("none")) {
            return "redirect:/user/login";
        }
        Event theEvent = eventDao.findOne(id);
        List<User> usersAttending = theEvent.getUsers();
        LocalDate todaysDate = LocalDate.now();
        if (todaysDate.isAfter(theEvent.getDate().toLocalDate())) {
            eventDao.delete(id);
            return "event/event-has-passed";
        }


        List<User> u = userDao.findByUsername(username);
        User currentUser = u.get(0);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", usersAttending);
        model.addAttribute("num_of_ppl", usersAttending.size());
        model.addAttribute("theEvent", theEvent);


        return "event/event-view";
    }

    @RequestMapping(value = "event-added/{id}", method = RequestMethod.GET)
    public String attendEventSubmission(Model model, @PathVariable int id, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }

        User u = userDao.findByUsername(username).get(0);
        Event addedEvent = eventDao.findOne(id);
        for (Event userEvent : u.getEvents()) {
            if (userEvent.getId() == addedEvent.getId()) {
                Event theEvent = eventDao.findOne(id);
                model.addAttribute("currentUser", u);
                model.addAttribute("theEvent", theEvent);
                model.addAttribute("duplicate_event_error", "This event has already been added.");
                return "event/event-view";
            }
        }

        u.addEvent(addedEvent);
        userDao.save(u);
        return "event/event-added";
    }

    @RequestMapping(value = "my-events")
    String displayMyEvents(Model model, @CookieValue(value = "user", defaultValue = "none") String username) {
        if (username.equals("none")) {
            return "redirect:/user/login";
        }

        User u = userDao.findByUsername(username).get(0);
        model.addAttribute("currentUser", u);
        List<Event> userEvents = u.getEvents();
        model.addAttribute("events", userEvents);


        return "event/my-events";
    }


    @RequestMapping(value = "remove-event", method = RequestMethod.GET)
    public String displayRemoveEventForm(Model model, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }

        User u = userDao.findByUsername(username).get(0);
        model.addAttribute("currentUser", u);
        model.addAttribute("events", u.getEvents());
        model.addAttribute("title", "Remove Events");
        return "event/remove-event";
    }

    //TODO: fix remove process form.
    @RequestMapping(value = "remove-event", method = RequestMethod.POST)
    public String processRemoveCheeseForm(@CookieValue(value = "user", defaultValue = "none") String username, @RequestParam int[] ids) {
        User u = userDao.findByUsername(username).get(0);
        List<Event> userEventsEdit = u.getEvents();
        for (int id : ids) {
            userEventsEdit.remove(eventDao.findOne(id));
        }

        u.setEvents(userEventsEdit);
        userDao.save(u);

        return "redirect:";
    }

    @RequestMapping(value = "google-maps/{id}")
    public String displayMap(Model model, @PathVariable int id, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }

        Event theEvent = eventDao.findOne(id);
        double latitude = theEvent.getLatitude();
        double longitude = theEvent.getLongitude();
        model.addAttribute("latitude", latitude);
        model.addAttribute("longitude", longitude);
        User u = userDao.findByUsername(username).get(0);
        model.addAttribute("currentUser", u);
        model.addAttribute("title", "Big ol' map");
        model.addAttribute("latlng", "{lat:" + -25.344 + "," + "lng:" + 131.036 + "}");


        return "event/google-maps";

    }
}