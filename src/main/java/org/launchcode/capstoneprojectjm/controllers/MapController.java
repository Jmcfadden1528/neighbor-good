package org.launchcode.capstoneprojectjm.controllers;

import org.launchcode.capstoneprojectjm.models.Data.AddressDao;
import org.launchcode.capstoneprojectjm.models.Data.EventDao;
import org.launchcode.capstoneprojectjm.models.Data.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("map")
public class MapController {


    @Autowired
    private EventDao eventDao; // Allows us to interact with database

    @Autowired
    AddressDao addressDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "")
    public String displayMap(Model model, @CookieValue(value = "user", defaultValue = "none") String username) {

        if (username.equals("none")) {
            return "redirect:/user/login";
        }

        model.addAttribute("title", "Big ol' fucking map");

        return "map/google-maps";

    }


}
