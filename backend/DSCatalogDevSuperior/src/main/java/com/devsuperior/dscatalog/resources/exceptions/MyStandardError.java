package com.devsuperior.dscatalog.resources.exceptions;

import java.io.Serializable;
import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyStandardError  implements Serializable{
		private static final long serialVersionUID = 1L;
		
		private Instant timestamp;
		private Integer status;
		private String error;
		private String message;
		private String path;
		
		public MyStandardError() {};
	}

