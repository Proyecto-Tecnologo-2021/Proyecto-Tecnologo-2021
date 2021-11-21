package proyecto2021G03.appettit.bean.restaurante;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtils;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IEstadisticasService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.DashMenuDTO;
import proyecto2021G03.appettit.dto.DashReclamoDTO;
import proyecto2021G03.appettit.dto.DashTotalDTO;
import proyecto2021G03.appettit.dto.EstadoPedido;
import proyecto2021G03.appettit.dto.PedidoDTO;
import proyecto2021G03.appettit.dto.RestauranteDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.exception.AppettitException;
import proyecto2021G03.appettit.util.Constantes;

@Named("HomeRestaurante")
//@SessionScoped
@RequestScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeRestauranteBean implements Serializable {

	static Logger logger = Logger.getLogger(HomeRestauranteBean.class);

	private static final long serialVersionUID = 1L;

	List<PedidoDTO> pedidos;
	List<CalificacionPedidoDTO> comentarios;
	List<DashReclamoDTO> reclamosDTO;
	List<DashMenuDTO> tendencias;
	List<DashMenuDTO> recientes;
	DashTotalDTO formapago;
	DashTotalDTO reclamos;
	DashTotalDTO estados;
	DashTotalDTO ventas;
	Boolean abierto;
	String fechaHora;
	RestauranteDTO restauranteDTO;
	Long id_restaurante;
	FacesContext facesContext;
	HttpSession session;
	LocalDateTime fechaHasta = LocalDateTime.now();;
	LocalDateTime fechaDesde = fechaHasta.minusDays(7);; 

	private Paint[] Colores = new Paint[] { new Color(0, 128, 55), new Color(242, 162, 44), new Color(213,51,67),new Color(213,51,67, 50), };
	private String[] fpHexaColores = new String[] { "#008037", "#F2A22C", };
	
	private Map<String, String> fpSytleLabel;

	@EJB
	IDepartamentoService departamentoService;

	@EJB
	IUsuarioService usrService;

	@EJB
	IEstadisticasService estadisitciasSrv;

	@PostConstruct
	public void init() {
		try {

			facesContext = FacesContext.getCurrentInstance();
			session = (HttpSession) facesContext.getExternalContext().getSession(true);
			
			UsuarioDTO usuarioDTO = getUserSession();

			if (usuarioDTO == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
			} else {
				restauranteDTO = usrService.buscarRestaurantePorId(usuarioDTO.getId());

				Date fechaBase = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				fechaHora = dateFormat.format(fechaBase);
				fpSytleLabel = new HashMap<String, String>();

				pedidos = estadisitciasSrv.listarPedidosPendientesPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta);
				comentarios = estadisitciasSrv.listarCalificacionesPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta, 3);
				tendencias = estadisitciasSrv.listarTendenciasPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta, 4);
				recientes = estadisitciasSrv.listarPediosRecientesPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta, 4);
				formapago = estadisitciasSrv.listarFormaPagoPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta);
				reclamos = estadisitciasSrv.listarReclamosTPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta); 
				estados  = estadisitciasSrv.listarEstadoPedidosPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta);
				reclamosDTO = estadisitciasSrv.listarReclamosPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta);
				ventas = estadisitciasSrv.listarVentasPorRestaurante(restauranteDTO.getId(), fechaDesde,
						fechaHasta, 7); 
				abierto = restauranteDTO.getAbierto();

			}

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		}

	}

	public void abrirRestaurante() throws AppettitException {
		logger.info("abrio");
		restauranteDTO = usrService.abrirRestaurante(restauranteDTO.getId());
		abierto = restauranteDTO.getAbierto();
	}

	public void cerrarRestaurante() throws AppettitException {
		logger.info("cerro");
		restauranteDTO = usrService.cerrarRestaurante(restauranteDTO.getId());
		abierto = restauranteDTO.getAbierto();
	}

	public UsuarioDTO getUserSession() {
		UsuarioDTO usuarioDTO = null;
		try {
			usuarioDTO = (UsuarioDTO) session.getAttribute(Constantes.LOGINUSUARIO);
		} catch (Exception e) {
			logger.error("Intento de acceso");
		}

		return usuarioDTO;

	}

	public String getFechaHora(LocalDateTime fecha, String formato) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formato);

		return fecha.format(formatter);

	}

	public String strFechaDesde() {
		return getFechaHora(this.fechaDesde, "dd/MM/yyyy");
	}

	public String strFechaHasta() {
		return getFechaHora(this.fechaHasta, "dd/MM/yyyy");
	}

	@SuppressWarnings("rawtypes")
	public StreamedContent getChartFormaPago() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					PieDataset dataset = createRingDataset(formapago.getData());
					JFreeChart chart = ChartFactory.createRingChart("", dataset, false, true, false);
					RingPlot pie = (RingPlot) chart.getPlot();

					pie.setBackgroundPaint(Color.WHITE);
					pie.setOutlineVisible(false);
					pie.setLabelGenerator(null);
					pie.setSectionPaint(dataset.getKey(0), Colores[0]);
					pie.setSectionPaint(dataset.getKey(1), Colores[1]);
					pie.setSectionDepth(0.33);
					pie.setSectionOutlinesVisible(false);
					pie.setShadowPaint(null);
					
					fpSytleLabel.put((String) dataset.getKey(0), "color:#fff;background:" + fpHexaColores[0] );
					fpSytleLabel.put((String) dataset.getKey(1), "color:#fff;background:" + fpHexaColores[1] );
					
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	@SuppressWarnings("rawtypes")
	public StreamedContent getChartReclamos() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					PieDataset dataset = createRingDataset(reclamos.getData());
					JFreeChart chart = ChartFactory.createRingChart("", dataset, false, true, false);
					RingPlot pie = (RingPlot) chart.getPlot();

					pie.setBackgroundPaint(Color.WHITE);
					pie.setOutlineVisible(false);
					pie.setLabelGenerator(null);
					pie.setSectionPaint(dataset.getKey(0), Colores[2]);
					pie.setSectionPaint(dataset.getKey(1), Colores[3]);
					pie.setSectionDepth(0.33);
					pie.setSectionOutlinesVisible(false);
					pie.setShadowPaint(null);
					
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public StreamedContent getChartEstados() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					PieDataset dataset = createRingDataset(estados.getData());
					JFreeChart chart = ChartFactory.createRingChart("", dataset, false, true, false);
					RingPlot pie = (RingPlot) chart.getPlot();

					pie.setBackgroundPaint(Color.WHITE);
					pie.setOutlineVisible(false);
					pie.setLabelGenerator(null);
					pie.setSectionDepth(0.33);
					pie.setSectionOutlinesVisible(false);
					pie.setShadowPaint(null);
					
					
					
					for(int d = 0; d< dataset.getKeys().size(); d++  ) {
						if (dataset.getKey(d).toString().equalsIgnoreCase(EstadoPedido.SOLICITADO.toString())) {
							pie.setSectionPaint(dataset.getKey(d), new Color(255, 255, 36));
						} else if (dataset.getKey(d).toString().equalsIgnoreCase(EstadoPedido.CONFIRMADO.toString())) {
							pie.setSectionPaint(dataset.getKey(d), new Color(255, 103, 0));
						} else if (dataset.getKey(d).toString().equalsIgnoreCase(EstadoPedido.ENVIADO.toString())) {
							pie.setSectionPaint(dataset.getKey(d), new Color(242, 162, 44));
						} else if (dataset.getKey(d).toString().equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())) {
							pie.setSectionPaint(dataset.getKey(d), new Color(11, 171, 100));
						} else if (dataset.getKey(d).toString().equalsIgnoreCase(EstadoPedido.RECHAZADO.toString())) {
							pie.setSectionPaint(dataset.getKey(d), new Color(255, 36, 0));
						} else if (dataset.getKey(d).toString().equalsIgnoreCase(EstadoPedido.CANCELADO.toString())) {
							pie.setSectionPaint(dataset.getKey(d), new Color(128,128,128));
						}
					}
					
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	@SuppressWarnings("rawtypes")
	public StreamedContent getChartVentasTotales() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					CategoryDataset dataset = createDataset(ventas.getData());
					
					
					final JFreeChart chart = ChartFactory.createStackedAreaChart(
				            "",      // chart title
				            "",                // domain axis label
				            "",                   // range axis label
				            dataset,                   // data
				            PlotOrientation.VERTICAL,  // orientation
				            false,                      // include legend
				            true,
				            false
				        );
					
					final CategoryPlot plot = (CategoryPlot) chart.getPlot();
					plot.setForegroundAlpha(0.5f);
			        plot.setBackgroundPaint(Color.WHITE);
			        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
			        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
					
					
										
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}).build();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	private PieDataset createRingDataset(Map<String, Double> datos) {
		DefaultPieDataset dataset = new DefaultPieDataset();
		
		for (Map.Entry<String, Double> entry : datos.entrySet()) {
			dataset.setValue(entry.getKey(), entry.getValue());
		}

		return dataset;
	}

	private DefaultCategoryDataset createDataset(Map<String, Double> datos) {
		
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	    String series = "ventas";
	    
        for (Map.Entry<String, Double> entry : datos.entrySet()) {
			logger.info("entry.getValue(): " + entry.getKey() + ":" + entry.getValue());
			dataset.addValue(entry.getValue(), series, entry.getKey());
		}

		//CategoryDataset dataset = DatasetUtils.createCategoryDataset("", "", data);
		
		return dataset;
	}

	
	public String styleLabel(String tipo, String key) {
		
		Color color = null;
		if (tipo.equalsIgnoreCase("fp")) {
			return fpSytleLabel.get(key);
		} else if (tipo.equalsIgnoreCase("es")) {
			if (key.equalsIgnoreCase(EstadoPedido.SOLICITADO.toString())) {
				color = new Color(255, 255, 36);
			} else if (key.equalsIgnoreCase(EstadoPedido.CONFIRMADO.toString())) {
				color = new Color(255, 103, 0);
			} else if (key.equalsIgnoreCase(EstadoPedido.ENVIADO.toString())) {
				color =  new Color(242, 162, 44);
			} else if (key.equalsIgnoreCase(EstadoPedido.ENTREGADO.toString())) {
				color =  new Color(11, 171, 100);
			} else if (key.equalsIgnoreCase(EstadoPedido.RECHAZADO.toString())) {
				color =  new Color(255, 36, 0);
			} else if (key.equalsIgnoreCase(EstadoPedido.CANCELADO.toString())) {
				color =  new Color(128,128,128);
			} else {
				color = new Color(255, 255, 255);
			}
			return  "color:#fff;background:#" + Integer.toHexString(color.getRGB()).substring(2);			
		}
		
		return null;
	}
	
}
