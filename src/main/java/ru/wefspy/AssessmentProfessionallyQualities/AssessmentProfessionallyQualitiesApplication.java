package ru.wefspy.AssessmentProfessionallyQualities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class AssessmentProfessionallyQualitiesApplication {

	public static void main(String[] args) {
		SpringApplication.run(AssessmentProfessionallyQualitiesApplication.class, args);
	}

}
