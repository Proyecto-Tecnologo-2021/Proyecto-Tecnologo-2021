package proyecto2021G03.appettit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashCalificacionResDTO {

	private Integer cal_1;
	private Integer cal_2;
	private Integer cal_3;
	private Integer cal_4;
	private Integer cal_5;
	private Integer general;
	private Integer cantidad;
	
}
