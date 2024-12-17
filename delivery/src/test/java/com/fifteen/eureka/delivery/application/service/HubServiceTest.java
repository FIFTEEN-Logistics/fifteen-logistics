package com.fifteen.eureka.delivery.application.service;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.fifteen.eureka.delivery.application.dto.hub.HubCreateRequest;
import com.fifteen.eureka.delivery.common.exceptionhandler.CustomApiException;
import com.fifteen.eureka.delivery.domain.model.Hub;

@SpringBootTest
@Transactional
class HubServiceTest {

	@Autowired
	private HubService hubService;

	@DisplayName("중앙허브를 생성할 수 있다.")
	@Test
	void createCentralHubTest() {
	    // given
		HubCreateRequest hubCreateRequest = HubCreateRequest.builder()
			.centralHubId(null)
			.hubManagerId(1L)
			.hubAddress("경기도 이천시 덕평로 257-21")
			.hubName("경기 남부 센터")
			.longitude(37.190892)
			.latitude(127.376218)
			.build();

		// when
		Hub hub = hubService.createHub(hubCreateRequest);

		// then
		assertThat(hub).isNotNull();
		assertThat(hub.getId()).isNotNull();
		assertThat(hub.getCentralHub()).isNull();
		assertThat(hub.getHubName()).isEqualTo("경기 남부 센터");
	}

	@DisplayName("중앙허브에 소속된 일반허브를 생성할 수 있다.")
	@Test
	void createGeneralHub() {
	    // given
		HubCreateRequest centralHubCreateRequest = HubCreateRequest.builder()
			.centralHubId(null)
			.hubManagerId(1L)
			.hubAddress("경기도 이천시 덕평로 257-21")
			.hubName("경기 남부 센터")
			.longitude(37.190892)
			.latitude(127.376218)
			.build();

		Hub centralHub = hubService.createHub(centralHubCreateRequest);

		HubCreateRequest generalHubCreateRequest = HubCreateRequest.builder()
			.centralHubId(centralHub.getId())
			.hubManagerId(2L)
			.hubAddress("서울특별시 송파구 송파대로 55")
			.hubName("서울특별시 센터")
			.longitude(37.480445)
			.latitude(127.121791)
			.build();

	    // when
		Hub generalHub = hubService.createHub(generalHubCreateRequest);

		// then
		assertThat(generalHub).isNotNull();
		assertThat(generalHub.getCentralHub().getId()).isEqualTo(centralHub.getId());
	}

	@DisplayName("DB에 저장되지 않은 중앙허브를 일반허브의 중앙허브에 등록하면 예외가 발생한다.")
	@Test
	void createGeneralHubWithNotExistedCentralHub() {
	    // given
		HubCreateRequest hubCreateRequest = HubCreateRequest.builder()
			.centralHubId(UUID.randomUUID())
			.hubManagerId(1L)
			.hubAddress("서울특별시 송파구 송파대로 55")
			.hubName("서울특별시 센터")
			.longitude(37.480445)
			.latitude(127.121791)
			.build();

	    // when
	    // then
		assertThatThrownBy(() -> hubService.createHub(hubCreateRequest))
			.isInstanceOf(CustomApiException.class)
			.hasMessage("Not Found");
	}
}