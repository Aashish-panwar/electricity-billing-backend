package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.ChangePasswordRequest;
import com.electricity.electricity_billing_backend.dto.request.UpdateProfileRequest;
import com.electricity.electricity_billing_backend.dto.response.ProfileResponse;

public interface ProfileService {

    ProfileResponse getProfile();

    ProfileResponse updateProfile(UpdateProfileRequest request);

    void changePassword(ChangePasswordRequest request);



}