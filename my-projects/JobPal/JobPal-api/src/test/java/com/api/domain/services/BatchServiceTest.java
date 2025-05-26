package com.api.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.api.domain.repositories.CertificateIssuanceDashBoardRepository;
import com.api.domain.repositories.CertificateIssuanceRepository;
import com.api.domain.repositories.JobSearchDashBoardRepository;
import com.api.domain.repositories.NotificationRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
class BatchServiceTest {

	@Autowired
	private BatchService target;

	@Mock
	private JobSearchDashBoardRepository jobSearchDashBoardRepository;

	@Mock
	private CertificateIssuanceDashBoardRepository certificateIssuanceDashBoardRepository;

	@Mock
	private CertificateIssuanceRepository issuanceRepository;

	@Mock
	private NotificationRepository notificationRepository;

	@Mock
	private Logger log;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
	}
}
//	@Test
//	void testExecuteBatch() {
//		doNothing().when(log).info(anyString());
//		doNothing().when(log).error(anyString());
//
//		target.executeBatch();
//
//		verify(log, times(2)).info(anyString());
//	}
//}
