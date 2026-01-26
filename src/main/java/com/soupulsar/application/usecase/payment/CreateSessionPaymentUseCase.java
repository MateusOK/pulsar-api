//package com.soupulsar.application.usecase.payment;
//
//import com.asaas.apisdk.AsaasSdk;
//import com.soupulsar.application.dto.request.AsaasCustomerRequest;
//import com.soupulsar.domain.model.payment.Payment;
//import com.soupulsar.domain.model.client.ClientProfile;
//import com.soupulsar.domain.model.session.Session;
//import com.soupulsar.domain.model.user.User;
//import com.soupulsar.domain.repository.ClientProfileRepository;
//import com.soupulsar.domain.repository.PaymentRepository;
//import com.soupulsar.domain.repository.SessionRepository;
//import com.soupulsar.domain.repository.UserRepository;
//import com.soupulsar.infrastructure.persistence.mapper.AsaasMapper;
//import lombok.RequiredArgsConstructor;
//
//import java.util.UUID;
//
//@RequiredArgsConstructor
//public class CreateSessionPaymentUseCase {
//
//    private final UserRepository userRepository;
//    private final ClientProfileRepository clientProfileRepository;
//    private final PaymentRepository paymentRepository;
//    private final SessionRepository sessionRepository;
//    private final AsaasSdk asaasService;
//
//    public Payment execute(UUID sessionId) {
//
//        Session session = sessionRepository.findBySessionId(sessionId)
//                .orElseThrow(() -> new IllegalArgumentException("Session not found"));
//        User client = userRepository.findById(session.getClientId())
//                .orElseThrow(() -> new IllegalArgumentException("Client not found"));
//        User specialist = userRepository.findById(session.getSpecialistId())
//                .orElseThrow(() -> new IllegalArgumentException("Specialist not found"));
//
//        String asaasCustomerId = ensureAsaasCustomer(client);
//
//        asaasService.payment.createNewPayment();
//
//        return null;
//
//    }
//
//    private String ensureAsaasCustomer(User client) {
//
//        ClientProfile clientProfile = clientProfileRepository.findById(client.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("Client profile not found"));
//
//        if (client.hasAsaasCustomerId()) {
//            return clientProfile.getAsaasCustomerId();
//        }
//
//        var response = asaasService.customer.createNewCustomer(AsaasMapper.toSdkDtoBuilder(customerRequest(client)));
//
//        clientProfile.setAsaasCustomerId(response.getId());
//        clientProfileRepository.save(clientProfile);
//
//        return response.getId();
//
//    }
//
//    private AsaasCustomerRequest customerRequest(User client) {
//        return AsaasCustomerRequest.builder()
//                .name(client.getName())
//                .email(client.getEmail())
//                .cpf(client.getCpf())
//                .phone(client.getTelephone())
//                .address(client.getAddress())
//                .externalReference(client.getUserId().toString())
//                .build();
//    }
//
//    private
//
//}