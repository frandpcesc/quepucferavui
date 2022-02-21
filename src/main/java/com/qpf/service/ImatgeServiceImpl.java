package com.qpf.service;

import java.util.Base64;

import org.springframework.stereotype.Service;

@Service
public class ImatgeServiceImpl implements ImatgeService{

	@Override
	public String getImgData(byte[] byteData) {
		return Base64.getMimeEncoder().encodeToString(byteData);
	}

}
