/**
 * Copyright (C) 2015 Stubhub.
 */
package io.bigdime.runtimeinfo;

import java.util.List;

import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.Mockito.*;

import org.springframework.test.util.ReflectionTestUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.bigdime.runtimeinfo.impl.RuntimeInfoRepositoryService;
import io.bigdime.runtimeinfo.DTO.RuntimeInfoDTO;
import io.bigdime.runtimeinfo.repositories.RuntimeInfoRepository;

public class RuntimeInfoRepositoryServiceTest {

	@Mock
	RuntimeInfoDTO mockRuntimeInfo;

	RuntimeInfoRepositoryService runtimeInfoRepositoryServce;

	@Mock
	RuntimeInfoRepository mockRuntimeInfoRepository;
	
	

	@BeforeClass
	public void init() {
		initMocks(this);
		runtimeInfoRepositoryServce = new RuntimeInfoRepositoryService();
		ReflectionTestUtils.setField(runtimeInfoRepositoryServce,
				"runtimeInfoRepository", mockRuntimeInfoRepository);
		when(mockRuntimeInfo.getAdaptorName()).thenReturn("testAdaptorName");
		when(mockRuntimeInfo.getEntityName()).thenReturn("testEntityName");
		when(mockRuntimeInfo.getInputDescriptor()).thenReturn("testDescriptor");
	}
	
	@Test
	public void testCreate() {
		when(
				mockRuntimeInfoRepository
						.findByAdaptorNameAndEntityNameAndInputDescriptor(
								mockRuntimeInfo.getAdaptorName(), mockRuntimeInfo.getEntityName(), mockRuntimeInfo.getInputDescriptor()))
				.thenReturn(null);
		Assert.assertTrue(runtimeInfoRepositoryServce.create(mockRuntimeInfo));
	}

	@Test
	public void testUpdate() {
		

		when(
				mockRuntimeInfoRepository
						.findByAdaptorNameAndEntityNameAndInputDescriptor(
								mockRuntimeInfo.getAdaptorName(), mockRuntimeInfo.getEntityName(), mockRuntimeInfo.getInputDescriptor()))
				.thenReturn(Mockito.mock(RuntimeInfoDTO.class));
		
		Assert.assertTrue(runtimeInfoRepositoryServce.create(mockRuntimeInfo));
	}

	@Test
	public void testGetAdapterEntries() {
		
		when(mockRuntimeInfoRepository.findByAdaptorNameAndEntityName(anyString(), anyString())).thenReturn(Mockito.mock(List.class));
		Assert.assertNotNull(runtimeInfoRepositoryServce.get("testAdaptorName", "testEntityName"));
	}
	
	@Test
	public void testGetAdaptorDescriptorEntry() {
		when(mockRuntimeInfoRepository.findByAdaptorNameAndEntityNameAndInputDescriptor(anyString(), anyString(), anyString())).thenReturn(mockRuntimeInfo);
		Assert.assertNotNull(runtimeInfoRepositoryServce.get("testAdaptorName", "testEntityName","testDescriptor"));
	}
}
