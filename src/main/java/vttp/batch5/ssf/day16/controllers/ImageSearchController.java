package vttp.batch5.ssf.day16.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import vttp.batch5.ssf.day16.services.ImageSearchService;

@Controller
public class ImageSearchController {
    @Autowired 
    ImageSearchService imgSearchSvc;

    @RequestMapping("/search")
    public String getImageSearch(@RequestParam MultiValueMap<String, String> form, Model model) {
        
        String query = form.getFirst("query");
        int limit = Integer.parseInt(form.getFirst("limit"));
        String rating = form.getFirst("rating");

        List<String> imageSrcResults = imgSearchSvc.getWithQueryParams(query, limit, rating);

        model.addAttribute("query", query);
        model.addAttribute("imageSrcResults", imageSrcResults);

        return "images";
    }
}
