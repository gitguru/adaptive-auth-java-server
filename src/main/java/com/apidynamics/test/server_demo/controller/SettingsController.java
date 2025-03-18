package com.apidynamics.test.server_demo.controller;

import com.apidynamics.test.server_demo.entity.ApiProvider;
import com.apidynamics.test.server_demo.repository.ApiProviderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class SettingsController {

	@Value("${spring.application.name}")
	private String appName;

	private final ApiProviderRepository apiProviderRepository;

	@Autowired
	public SettingsController(ApiProviderRepository apiProviderRepository) {
		this.apiProviderRepository = apiProviderRepository;
	}

	@RequestMapping("/")
	public String homePage(Model model) {
		Optional<ApiProvider> apiProvider = apiProviderRepository.findById(1);
		model.addAttribute("appName", appName);
        apiProvider.ifPresent(provider -> model.addAttribute("providerId", provider));
		return "home";
	}

	@PostMapping("/settings")
	public String save(@RequestParam(value = "provider_id", defaultValue = "Unknown") String providerId) {
		apiProviderRepository.save(ApiProvider.builder().id(1).publicKey(providerId).build());
		return "redirect:/";
	}

}
