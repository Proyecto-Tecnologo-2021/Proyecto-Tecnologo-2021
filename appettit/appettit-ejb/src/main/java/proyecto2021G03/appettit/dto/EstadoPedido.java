package proyecto2021G03.appettit.dto;

import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType
public enum EstadoPedido {

	SOLICITADO,
	CONFIRMADO,
	RECHAZADO,
	ENVIADO,
	ENTREGADO,
	CANCELADO
}
