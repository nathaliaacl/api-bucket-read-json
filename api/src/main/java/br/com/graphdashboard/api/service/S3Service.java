package br.com.graphdashboard.api.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import br.com.graphdashboard.api.exception.InternalServerError;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    @Value("${s3.bucket.suffix}")
    private String s3BucketSuffix;

    public String readJsonFromS3(String user, String client) {

        String bucket = "bucket-" + user + "-" + s3BucketSuffix;
        String path = client + "/file.json";

        if (!amazonS3.doesBucketExistV2(bucket)) {
            throw new InternalServerError("The bucket " + bucket + " does not exist");
        }
        if (!amazonS3.doesObjectExist(bucket, path)) {
            throw new InternalServerError("The file " + path + " was not found in the bucket " + bucket);
        }

        S3Object s3Object = amazonS3.getObject(bucket, path);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(s3Object.getObjectContent()))) {
            String jsonContent = reader.lines().collect(Collectors.joining("\n"));
            
            if (jsonContent.isEmpty()) {
                throw new InternalServerError("Empty dashboard.json file");
            }
            jsonContent = jsonContent.replace("'", "\"");

            return jsonContent;
        } catch (IOException e) {            
            e.printStackTrace(); 
            return null;
        }
    }
}
