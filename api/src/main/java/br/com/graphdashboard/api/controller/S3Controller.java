package br.com.graphdashboard.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import br.com.graphdashboard.api.service.S3Service;

@RestController
public class S3Controller {

    @Autowired
    private S3Service s3Service;

    @GetMapping("/graphs/{user}/{client}")
    public String readJsonFromS3(@PathVariable String user, @PathVariable String client) {
        return s3Service.readJsonFromS3(user, client);
    }

}
