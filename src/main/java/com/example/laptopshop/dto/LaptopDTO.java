package com.example.laptopshop.dto;

import lombok.Data;

@Data

public class LaptopDTO {
    private int id;
    private String link;
    private String cpuName;
    private String cores;
    private String threads;
    private String baseSpeed;

    private String maxSpeed;

    private String name;

    private String model;

    private Double originalPrice;

    private Double currentPrice;

    private String discount;

    private String description;

    private String size;

    private String materialType;

    private String batterySystem;

    private String osSystem;

    private String releaseTime;

    private String connectivityPorts;

    private String wireless;

    private String webcams;

    private String features;

    private String keyboardLights;

    private String ramType;

    private String ramSpeed;

    private String maxRamSize;

    private String hardDrive;

    private String review;

    private String imageUrl;
    private String screenType;
    private String resolution;
    private String refreshRate;
    private String colorDepth;
    private String screenTechnology;
    private String screenCard;
    private String soundTechnology;
    private boolean deleted;
}
