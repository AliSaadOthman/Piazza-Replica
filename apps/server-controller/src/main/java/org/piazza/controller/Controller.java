package org.piazza.controller;

import org.json.JSONException;
import org.piazza.reply_template_abstraction.CompletableFutureReplyKafkaTemplate;
import org.piazza.repo.CacheServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class Controller {

    @Autowired
    private CompletableFutureReplyKafkaTemplate<String, Map<String, Object>, Map<String, Object>> requestReplyKafkaTemplate;

    @Autowired
    CacheServer cacheServer;

    @Async
    @PostMapping("/api/login") // CourseApp/AddQuestion (CourseApp -> course.topic)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getObject2(@RequestBody Map<String, Object> jsonObject, @RequestAttribute(value = "url", required = false) String url)
            throws InterruptedException, ExecutionException {
        Map<String, Object> objectMap = new Hashtable<>();

        if(jsonObject.containsKey("url")){
            jsonObject.remove("url");
        }
        jsonObject.put("url",url);
        objectMap.put("body",jsonObject);
        objectMap.put("commandName", "LoginCommand");
        CompletableFuture<Map<String, Object>> reply = requestReplyKafkaTemplate.requestReply("user.topic", objectMap);
        Map<String, Object> response = reply.get();
        if(response.containsKey("access_token")){
            tokenSaver((String) response.get("access_token"));
        }
        else{
            return CompletableFuture.completedFuture(new ResponseEntity<>(reply.get(), HttpStatus.NOT_FOUND));
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(reply.get(), HttpStatus.OK));
    }
    @Async
    @PostMapping("/api/register") // CourseApp/AddQuestion (CourseApp -> course.topic)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> reg(@RequestBody Map<String, Object> jsonObject)
            throws InterruptedException, ExecutionException {
        Map<String, Object> objectMap = new Hashtable<>();
        objectMap.put("body",jsonObject);
        objectMap.put("commandName", "SignUpCommand");
        CompletableFuture<Map<String, Object>> reply = requestReplyKafkaTemplate.requestReply("user.topic", objectMap);
        return CompletableFuture.completedFuture(new ResponseEntity<>(reply.get(), HttpStatus.OK));
    }
    @Async
    @PostMapping("/api/logout") // CourseApp/AddQuestion (CourseApp -> course.topic)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> logout(@RequestAttribute(value = "access_token", required = false) String token)
            throws InterruptedException, ExecutionException {
        Map<String, Object> response = new HashMap<>();
        tokenRemover(token);
        return CompletableFuture.completedFuture(new ResponseEntity<>(response, HttpStatus.OK));
    }

    public void tokenSaver(String token){
        cacheServer.save("tokens", token, "this");
    }
    public void tokenRemover(String token){
        cacheServer.delete("tokens", token);
    }

    @Async
    @CrossOrigin
    @PostMapping("/api/refresh") // CourseApp/AddQuestion (CourseApp -> course.topic)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> regToken(@RequestBody Map<String, Object> jsonObject, @RequestAttribute(value = "url", required = false) String url)
            throws InterruptedException, ExecutionException {
        Map<String, Object> objectMap = new Hashtable<>();

        if(jsonObject.containsKey("url")){
            jsonObject.remove("url");
        }
        jsonObject.put("url",url);
        objectMap.put("body",jsonObject);
        objectMap.put("commandName", "RefreshTokenCommand");
        CompletableFuture<Map<String, Object>> reply = requestReplyKafkaTemplate.requestReply("user.topic", objectMap);
        Map<String, Object> response = reply.get();
        if(response.containsKey("access_token")){
            tokenSaver((String) response.get("access_token"));
        }
        else{
            return CompletableFuture.completedFuture(new ResponseEntity<>(reply.get(), HttpStatus.NOT_FOUND));
        }
        return CompletableFuture.completedFuture(new ResponseEntity<>(reply.get(), HttpStatus.OK));
    }

    @Async
    @RequestMapping(value = "/{endPoint}", method = RequestMethod.POST)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> postEndPoint(@RequestBody(required = false) Map<String, Object> jsonObject, @PathVariable String endPoint, @RequestParam(value = "param1", required = false) String param1, @RequestAttribute(value = "userEmail", required = false) String userEmail, @RequestAttribute(value = "userRole", required = false) String userRole) throws JSONException, ExecutionException, InterruptedException {

        Map<String, Object> objectMap = new Hashtable<>();
        if(jsonObject.containsKey("userEmail")){
            jsonObject.remove("userEmail");
        }
        if(jsonObject.containsKey("userRole")){
            jsonObject.remove("userRole");
        }
        jsonObject.put("userEmail",userEmail);
        jsonObject.put("userRole",userRole);
        objectMap.put("body",jsonObject);
        objectMap.put("commandName", param1);
        CompletableFuture<Map<String, Object>> reply = requestReplyKafkaTemplate.requestReply(endPoint + ".topic", objectMap);
        Map<String, Object> response = reply.get();

        return CompletableFuture.completedFuture(new ResponseEntity<>(response, HttpStatus.OK));
    }

    @Async
    @RequestMapping(value = "/{endPoint}", method = RequestMethod.GET)
    public CompletableFuture<ResponseEntity<Map<String, Object>>> getEndPoint(@RequestBody(required = false) Map<String, Object> jsonObject, @PathVariable String endPoint, @RequestParam(value = "param1", required = false) String param1, @RequestAttribute(value = "userEmail", required = false) String userEmail, @RequestAttribute(value = "userRole", required = false) String userRole) throws JSONException,InterruptedException, ExecutionException {

        Map<String, Object> objectMap = new Hashtable<>();
        if(jsonObject.containsKey("userEmail")){
            jsonObject.remove("userEmail");
        }
        if(jsonObject.containsKey("userRole")){
            jsonObject.remove("userRole");
        }
        jsonObject.put("userEmail",userEmail);
        jsonObject.put("userRole",userRole);
        objectMap.put("body",jsonObject);
        objectMap.put("commandName", param1);
        CompletableFuture<Map<String, Object>> reply = requestReplyKafkaTemplate.requestReply(endPoint + ".topic", objectMap);
        return CompletableFuture.completedFuture(new ResponseEntity<>(reply.get(), HttpStatus.OK));
    }
}