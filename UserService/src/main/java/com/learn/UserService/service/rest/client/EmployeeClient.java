package com.learn.UserService.service.rest.client;

import com.learn.UserService.dto.EmployeeDto;
import com.learn.UserService.exception.EmptyEmailException;
import com.learn.UserService.exception.UserExistsException;
import com.learn.UserService.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.redis.core.TimeToLive;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static java.util.Arrays.asList;

@Slf4j
@Service
public class EmployeeClient {

    private static final String EMPLOYEE_GET_URL = "/getAllProfile";
    private static final String EMPLOYEE_GET_BY_EMAIL_URL = "/getProfileByEmail/{email}";
    private static final String EMPLOYEE_CREATE_URL = "/addProfile";
    private static final String EMPLOYEE_UPDATE_URL = "/updateProfile";
    private static final String EMPLOYEE_DELETE_URL = "/deleteProfile/{email}";
    private final RestTemplate restTemplate = new RestTemplate();
    private final String employeeServiceBaseUrl;


    public EmployeeClient(@Value("${employee.service.base.uri}") String employeeServiceBaseUrl) {
        this.employeeServiceBaseUrl = employeeServiceBaseUrl;
    }

    @Cacheable(cacheNames = "emp")
    public List<EmployeeDto> getAllEmployeeProfiles()
    {
        EmployeeDto[] employeeObjects = restTemplate
                .getForObject(getEmployeeUrl(), EmployeeDto[].class);
        return asList(Objects.requireNonNull(employeeObjects));
    }

    @Cacheable(key = "#email" ,cacheNames = "emp")
    public EmployeeDto getProfileByEmail(String email){
        try{
            ResponseEntity<EmployeeDto> responseEntity = restTemplate.exchange(getEmployeeByEmailUrl(),
                    HttpMethod.GET,
                    new HttpEntity<>(httpHeaders()),
                    EmployeeDto.class, email);
            return responseEntity.getBody();
        }
        catch (Exception e){
            throw new UserNotFoundException("Profile Not Found");
        }
    }

    @CachePut(key = "#employeeDto.email",cacheNames = "emp")
    public EmployeeDto createEmployeeProfile(EmployeeDto employeeDto)
    {
        try{
            HttpEntity<EmployeeDto> request = prepareRequest(employeeDto);
            ResponseEntity<EmployeeDto> responseEntity = restTemplate.postForEntity(
                    createEmployeeUrl(),
                    request,
                    EmployeeDto.class);
            return Objects.requireNonNull(responseEntity.getBody());
        }
        catch (Exception e){
            if(e.getMessage().equalsIgnoreCase("409 : [Profile Already exists With Email]"))
            {
                log.info(e.getMessage());
                throw new UserExistsException("User Profile Already Exists");
            }
            else
            {
                log.info(e.getMessage());
                throw new EmptyEmailException("Email should be provided to create profile");
            }
        }
    }

    @CachePut(key = "#employeeDto.email",cacheNames = "emp")
    public EmployeeDto updateEmployeeProfile(EmployeeDto employeeDto){
        try{
            HttpEntity<EmployeeDto> request = prepareRequest(employeeDto);
            ResponseEntity<EmployeeDto> updateResponseEntity = restTemplate.exchange(
                    updateEmployeeUrl(),
                    HttpMethod.PUT,
                    request,
                    EmployeeDto.class);
            return updateResponseEntity.getBody();
        }
        catch (Exception e){
            throw new UserNotFoundException("User Not Found");
        }
    }

    @CacheEvict(key = "#email",cacheNames = "emp")
    public String deleteEmployeeProfile(String email){
        try{
            ResponseEntity<String> deleteResponseEntity = restTemplate.exchange(
                    deleteEmployeeUrl(),
                    HttpMethod.DELETE,
                    new HttpEntity<>(httpHeaders()),
                    String.class,
                    email
            );
        return deleteResponseEntity.getBody();
        }
        catch (Exception e) {
            throw new UserNotFoundException("User Not Found");
        }
    }


//    private RestTemplate createRestTemplate(RestTemplate restTemplate){
//        HttpComponentsClientHttpRequestFactory clientRequestFactory = new HttpComponentsClientHttpRequestFactory();
//        clientRequestFactory.setConnectTimeout(0);
//        restTemplate.setRequestFactory(clientRequestFactory);
//        return restTemplate;
//    }

    protected String getEmployeeUrl(){
        return employeeServiceBaseUrl + EMPLOYEE_GET_URL;
    }

    protected String createEmployeeUrl(){
        return employeeServiceBaseUrl + EMPLOYEE_CREATE_URL;
    }

    protected String updateEmployeeUrl(){
        return employeeServiceBaseUrl + EMPLOYEE_UPDATE_URL;
    }

    protected String deleteEmployeeUrl(){
        return employeeServiceBaseUrl + EMPLOYEE_DELETE_URL;
    }

    protected String getEmployeeByEmailUrl(){return employeeServiceBaseUrl + EMPLOYEE_GET_BY_EMAIL_URL;}

    HttpEntity<EmployeeDto> prepareRequest(EmployeeDto employeeDto){
        return new HttpEntity<>(employeeDto,httpHeaders());
    }

    private HttpHeaders httpHeaders(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

//    private HttpHeaders delHttpHeaders(String email){
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.valueOf(email));
//        return httpHeaders;
//    }

}
