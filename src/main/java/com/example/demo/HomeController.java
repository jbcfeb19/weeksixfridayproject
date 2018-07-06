package com.example.demo;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Controller
public class HomeController {

    @Autowired
    CarRepository carRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listMessages(@ModelAttribute Car car, @ModelAttribute Category category, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("cars", carRepository.findAll());
        return "list";
    }

    @GetMapping("/addcategory")
    public String loadForm(Model model) {
        model.addAttribute("category", new Category());
        return "category";
    }

    @PostMapping("/processcategory")
    public String processForm(@ModelAttribute Category category) {
        categoryRepository.save(category);
        return "redirect:/addcar";
    }

    @GetMapping("/addcar")
    public String loadForm(Model model, @ModelAttribute Category acategory) {
        model.addAttribute("imageLabel", "Upload Image");
        model.addAttribute("car", new Car());
        model.addAttribute("categories", categoryRepository.findAll());
        return "form";
    }

    @PostMapping("/processcar")
    public String processForm(@ModelAttribute Car car, @RequestParam
            ("file")
            MultipartFile file, @RequestParam("hiddenImgURL") String ImgURL) {

        if(!file.isEmpty()) {
            try {
                Map uploadResult = cloudc.upload(file.getBytes(),
                        ObjectUtils.asMap("resourcetype", "auto"));
                car.setCarURL(uploadResult.get("url").toString());

            } catch (IOException e) {
                e.printStackTrace();
                return "redirect:/addcar";
            }
        }

        else {
            if(!ImgURL.isEmpty()) {
                car.setCarURL(ImgURL);
            }
            else {
                car.setCarURL("");
            }
        }

        carRepository.save(car);
        return "redirect:/";
    }

    @RequestMapping("/detailcategory/{id}")
    public String viewCategory(@PathVariable("id") long id, Model model) {
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("category", categoryRepository.findById(id).get());
        return "categorydetail";
    }

    @RequestMapping("/detail/{id}")
    public String viewCar(@PathVariable("id") long id, Model model) {
        model.addAttribute("car", carRepository.findById(id).get());
        return "detail";
    }

    @RequestMapping("/update/{id}")
    public String updateCar(@ModelAttribute Car car, @ModelAttribute Category category, @PathVariable("id") long id,
    Model model){
        model.addAttribute("categories", categoryRepository.findAll());
        car = carRepository.findById(id).get();
        model.addAttribute("car", carRepository.findById(id));
        model.addAttribute("imageURL", car.getCarURL());

        if(car.getCarURL().isEmpty()) {
            model.addAttribute("imageLabel", "Upload Image");
        }
        else {
            model.addAttribute("imageLabel", "Upload New Image");
        }
        return "form";
    }

    @RequestMapping("/delete/{id}")
    public String delCar(@PathVariable("id") long id) {
        carRepository.deleteById(id);
        return "redirect:/";
    }
}
