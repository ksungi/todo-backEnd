package com.example.todo.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ResponseDTO<T> {
	//<T> - List 타입이 정해져있지 않았을 때
	private String error;
	private List<T> data; //얘도 같음
}
