package com.example.project_ee297.Controller;

/*
@RequestMapping("/users")
@RestController

public class Controller {

     @Autowired
    private UserRepository userRepository;
    @GetMapping("/SubscribeAccount")
    public String SubscribeAccount() {
        try {
            mqttService.acceptAccount("pub_account");
        } catch (Exception e) {
        }
        return "Success to subscribe account";
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        // check if user already exists
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        // create new user
        User newUser=new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        userRepository.save(newUser);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        String username = user.getUsername();
        String password = user.getPassword();
        // check if user exists
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (!existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // check if password matches
        if (!existingUser.get().getPassword().equals(password)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().build();
    }

    String filePath="C:\\Users\\Lenovo\\Pictures\\Screenshots\\savedImage.jpg";
    @PostMapping("/saveImageInJPG")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        byte[] fileContent = file.getBytes();
        // File newFile = new File(getCurrentUser().getPhotoPath());
        File newFile = new File(filePath );
        Files.write(newFile.toPath(), fileContent);
        return "File uploaded successfully!";
    }

    @GetMapping("/SubscribeImgAPP")
    public String SubscribeImgFromAPP() {
        try {
            mqttService.subscribeImgFromAPP("pub_image");
        } catch (Exception e) {
        }
        return "Success to subscribe IMG";
    }
    @SneakyThrows
    @RequestMapping(value = "/saveImage", method = RequestMethod.POST)
    @ResponseBody
    public String saveImage(@RequestBody  String base64Image) {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(base64Image);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String ImageBase64Code = Objects.requireNonNull(root).get("base64Image").asText();
        byte[] imageByte = Base64.decodeBase64(ImageBase64Code);
        File imageFile = new File(filePath );
        // File imageFile = new File(getCurrentUser().getPhotoPath());
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile))) {
            outputStream.write(imageByte);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to save image!";
        }
        return "save image successfully";
    }

    @SneakyThrows
    @RequestMapping(value = "/compare-images", method = RequestMethod.POST)
    @ResponseBody
    public String compareFace(@RequestBody  String base64Image) {
        String imagePath = "C:\\Users\\Lenovo\\Pictures\\Screenshots\\currentImage.jpg";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = mapper.readTree(base64Image);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String ImageBase64Code = Objects.requireNonNull(root).get("base64Image").asText();
        WebFaceCompare webFaceCompare=new WebFaceCompare("https://api.xf-yun.com/v1/private/s67c9c78c","c42a8fc5","NDRkOGEwYWQwMzkwYTM2OTc2M2RmODYx", "1389aa5d53e53ae7578609062dffe3b9",imagePath,filePath,"s67c9c78c");
        byte[] imageBytes = Base64.decodeBase64(ImageBase64Code);
        File imageFile = new File(imagePath);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imageFile))) {
            outputStream.write(imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to save current image!";
        }
        return webFaceCompare.similarity(imagePath,filePath);
        //return   webFaceCompare.similarity(imagePath,getCurrentUser().getPhotoPath());
    }
    @Autowired
    MqttService mqttService;
    @GetMapping("/SubscribeImg")
    public String mqttSubscribeImg() {
        try {
            mqttService.subscribeImg("Img");
        } catch (Exception e) {
        }
        return "Success";
    }
    @GetMapping("/SubscribeAudio")
    public String mqttSubscribeAudio() {
        try {
            mqttService.AcceptMP3("Audio");
        } catch (Exception e) {
        }
        return "Success";
    }
    @GetMapping("/SubscribeAudioAPP")
    public String mqttSubscribeAudioAPP() {
        try {
            mqttService.AcceptFormAPP("pub_audio");
        } catch (Exception e) {
        }
        return "Success";
    }


    private MqttClient mqttClient;
    @Autowired
    private Convert audioConversionService;
    @RequestMapping(value = "/test2", method = RequestMethod.POST)
    public String compareVoice(@RequestBody byte[] waveAudio) {
        String audioPathWave = "C:\\Users\\Lenovo\\Music\\Audio\\CurrentAudio.wave";
        String audioPathMp3 = "C:\\Users\\Lenovo\\Music\\Audio\\CurrentAudio.mp3";
        try {
            // 将音频数据写入MP3文件
            FileOutputStream fos = new FileOutputStream(audioPathWave);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(waveAudio);
            bos.flush();
            bos.close();
             audioConversionService.convertWavToMp3(audioPathWave, audioPathMp3);
            System.out.println("音频已保存为MP3文件：" + audioPathMp3);
        } catch (Exception e) {
            System.out.println("保存音频为MP3文件时发生错误：" + e.getMessage());
        }

        VoiceprintRecognition voiceprintRecognition = new VoiceprintRecognition();
        voiceprintRecognition.VoicePrintCompare(audioPathMp3);
        return "Successful";
    }

    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String saveVoice(@RequestBody byte[] waveAudio) {
        String audioPathWave = "C:\\Users\\Lenovo\\Music\\Audio\\UserAudio.wave";
        String audioPathMp3 = "C:\\Users\\Lenovo\\Music\\Audio\\UserAudio.mp3";
        try {
            // 将音频数据写入MP3文件
            FileOutputStream fos = new FileOutputStream(audioPathWave);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(waveAudio);
            bos.flush();
            bos.close();
            audioConversionService.convertWavToMp3(audioPathWave, audioPathMp3);
            System.out.println("音频已保存为MP3文件：" + audioPathMp3);
        } catch (Exception e) {
            System.out.println("保存音频为MP3文件时发生错误：" + e.getMessage());
        }

        VoiceprintRecognition voiceprintRecognition = new VoiceprintRecognition();
        voiceprintRecognition.VoicePrintCompare(audioPathMp3);
        return "Successful";
    }


}*/
