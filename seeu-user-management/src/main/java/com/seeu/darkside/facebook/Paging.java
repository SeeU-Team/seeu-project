package com.seeu.darkside.facebook;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Paging {

	private Cursors cursors;
	private String previous;
	private String next;
}
