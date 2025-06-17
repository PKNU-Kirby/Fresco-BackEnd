package fresco.com.refrigerator.service;

import fresco.com.global.exception.RestApiException;
import fresco.com.global.response.error.AuthErrorCode;
import fresco.com.global.response.error.RefrigeratorErrorCode;
import fresco.com.refrigerator.controller.dto.request.CreateRefrigeratorRequest;
import fresco.com.refrigerator.controller.dto.request.DeleteRefrigeratorRequest;
import fresco.com.refrigerator.controller.dto.request.GetAllRefrigeratorRequest;
import fresco.com.refrigerator.controller.dto.request.UpdateRefrigeratorRequest;
import fresco.com.refrigerator.controller.dto.response.RefrigeratorInfoResponse;
import fresco.com.refrigerator.domain.Refrigerator;
import fresco.com.refrigerator.domain.RefrigeratorUser;
import fresco.com.refrigerator.domain.repository.RefrigeratorRepository;
import fresco.com.refrigerator.domain.repository.RefrigeratorUserRepository;
import fresco.com.user.domain.User;
import fresco.com.user.domain.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class RefrigeratorService {
    private final UserRepository userRepository;
    private final RefrigeratorRepository refrigeratorRepository;
    private final RefrigeratorUserRepository refrigeratorUserRepository;

    @Transactional
    public RefrigeratorInfoResponse createRefrigerator(@Valid CreateRefrigeratorRequest request) {
        Refrigerator refrigerator = new Refrigerator(request.name());
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RestApiException(AuthErrorCode.NULL_USER));

        Refrigerator savedRefrigerator = refrigeratorRepository.save(refrigerator);
        refrigeratorUserRepository.save(new RefrigeratorUser(refrigerator, user));
        return new RefrigeratorInfoResponse(savedRefrigerator.getId(), savedRefrigerator.getName());
    }

    @Transactional
    public String deleteRefrigerator(@Valid DeleteRefrigeratorRequest request) {
        refrigeratorRepository.deleteById(request.refrigeratorId());
        refrigeratorUserRepository.deleteByRefrigeratorId(request.refrigeratorId());
        return "성공적으로 삭제되었습니다.";
    }

    @Transactional
    public RefrigeratorInfoResponse updateRefrigerator(@Valid UpdateRefrigeratorRequest request) {
        Refrigerator refrigerator = refrigeratorRepository.findById(request.refrigeratorId())
                .orElseThrow(() -> new RestApiException(RefrigeratorErrorCode.NULL_REFRIGERATOR));

        refrigerator.changeName(request.name());
        Refrigerator savedRefrigerator = refrigeratorRepository.save(refrigerator);
        return new RefrigeratorInfoResponse(savedRefrigerator.getId(), savedRefrigerator.getName());
    }

    @Transactional(readOnly = true)
    public List<RefrigeratorInfoResponse> getAllRefrigerator(GetAllRefrigeratorRequest request) {
        return refrigeratorUserRepository.findAllByUserId(request.userId());
    }
}
