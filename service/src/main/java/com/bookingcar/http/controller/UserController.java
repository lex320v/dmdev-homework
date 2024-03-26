package com.bookingcar.http.controller;

import com.bookingcar.dto.UserCreateEditDto;
import com.bookingcar.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public String findAll(Model model) {
        model.addAttribute("users", userService.findAll());

        return "user/users";
    }

    @GetMapping("/{id}")
    public String findById(@PathVariable("id") Long id, Model model) {
        model.addAttribute("user", userService.findById(id));

        return "user/user";
    }

    @PostMapping
    public String createUser(UserCreateEditDto userCreateEditDto) {
        userService.create(userCreateEditDto);

        return "redirect:/users/" + 25;
    }

//    @PutMapping("/{id}")
    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") Long id, UserCreateEditDto userCreateEditDto) {
        userService.update(id, userCreateEditDto);

        return "redirect:/users/{id}";
    }

//    @DeleteMapping("/{id}")
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Long id) {
        userService.delete(id);

        return "redirect:/users";
    }
}
