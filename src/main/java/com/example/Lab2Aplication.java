package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@RestController
@RequestMapping("/")
public class Lab2Aplication {
 private List<String> taskList = new ArrayList<>();

 @GetMapping("task")
 public List<String> getTasks() {
  return taskList;
 }

 @PostMapping("task")
 public String addTask(@RequestBody String task) {
  taskList.add(task);
  return "Task added successfully: " + task;
 }

 private Map<Integer, String> users = new HashMap<>();

 @GetMapping("add")
 public Map<Integer, String> getUsers() {
  return users;
 }

 @PostMapping("add")
 public String addUser(@RequestParam int id, @RequestParam String name) {
  users.put(id, name);
  return "User added successfully: " + name;
 }

 public static void main(String[] args) {
  SpringApplication.run(Lab2Aplication.class, args);
 }
}