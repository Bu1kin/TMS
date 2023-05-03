package com.example.TMS.Post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/post")
public class PostController {
    @Autowired
    PostRepo postRepo;

    @GetMapping("/all")
    public String viewPost(Model model) {
        Iterable<Post> posts = postRepo.findAll();
        model.addAttribute("posts", posts);
        return "/Admin/post/index";
    }

    @PostMapping("/add")
    public String postAdd(Post post, Model model){
        postRepo.save(post);
        return ("redirect:/post/all");
    }

    @PostMapping("/edit/{id}")
    public String postEdit(Post post, BindingResult result) {
        if(result.hasErrors())
            return ("/Admin/post/details");
        postRepo.save(post);
        return("redirect:/post/all");
    }

    @GetMapping("/del/{id}")
    public String postDelete(@PathVariable Long id) {
        postRepo.deleteById(id);
        return("redirect:/post/all");
    }

    @GetMapping("/details/{id}")
    public String postDetails(Model model, @PathVariable Long id) {
        Post post = postRepo.findById(id).orElseThrow();
        model.addAttribute("post", post);
        return "/Admin/post/details";
    }

    @GetMapping("/filter-contains")
    public String postFilterContains(@RequestParam String searchName,
                                         Model model){
        List<Post> posts = postRepo.findByPostNameContaining(searchName);
        model.addAttribute("posts", posts);
        return "/Admin/post/filter";
    }
}
