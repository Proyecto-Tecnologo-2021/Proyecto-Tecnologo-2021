package proyecto2021G03.appettit.bean.restaurante;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StackedBarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.user.IUserSession;
import proyecto2021G03.appettit.business.IDepartamentoService;
import proyecto2021G03.appettit.business.IEstadisticasService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.CalificacionPedidoDTO;
import proyecto2021G03.appettit.dto.DashGeoDTO;
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
	DashTotalDTO clientes;
	DashTotalDTO ordenes;
	DashTotalDTO pedidosPromedio;
	DashTotalDTO rapidez;
	DashTotalDTO comida;
	DashTotalDTO servicio;
	DashGeoDTO influencia;
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
	
	@EJB
	IUserSession usrSession;

	@PostConstruct
	public void init() {
		try {
			abierto = false;
			
			facesContext = FacesContext.getCurrentInstance();
			//ExternalContext externalContext = facesContext.getExternalContext();
			session = (HttpSession) facesContext.getExternalContext().getSession(true);
			
			usrSession.getRestauranteReg();
			
			UsuarioDTO usuarioDTO = getUserSession();

			if (usuarioDTO == null) {
				//externalContext.invalidateSession();
				//externalContext.dispatch(Constantes.REDIRECT_URI);
				//externalContext.redirect(Constantes.REDIRECT_URI);
				logout();
				
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
			} else {
				if(!(usuarioDTO instanceof RestauranteDTO)) {
					//externalContext.invalidateSession();
					//externalContext.dispatch(Constantes.REDIRECT_URI);
					//externalContext.redirect(Constantes.REDIRECT_URI);
					logout();
					
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
				} else {
					Date fechaBase = new Date();
					DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
					fechaHora = dateFormat.format(fechaBase);
					fpSytleLabel = new HashMap<String, String>();
					
					restauranteDTO = (RestauranteDTO) usuarioDTO;

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
					clientes = estadisitciasSrv.listarClientesPorRestaurante(restauranteDTO.getId(), fechaDesde,
							fechaHasta, 7);
					ordenes = estadisitciasSrv.listarOrdenesPorRestaurante(restauranteDTO.getId(), fechaDesde,
							fechaHasta, 7);
					pedidosPromedio = estadisitciasSrv.listarOrdenesPromedioPorRestaurante(restauranteDTO.getId(), fechaDesde,
							fechaHasta, 7);
					rapidez = estadisitciasSrv.listarCalificacionesDetPorRestaurante(restauranteDTO.getId(), fechaDesde,
							fechaHasta, "rapidez"); 
					comida = estadisitciasSrv.listarCalificacionesDetPorRestaurante(restauranteDTO.getId(), fechaDesde,
							fechaHasta, "comida"); 
					servicio = estadisitciasSrv.listarCalificacionesDetPorRestaurante(restauranteDTO.getId(), fechaDesde,
							fechaHasta, "servicio"); 
					influencia = estadisitciasSrv.listarGeoEntregasPorRestaurante(restauranteDTO.getId(), fechaDesde,
							fechaHasta);
					abierto = restauranteDTO.getAbierto();
				}

			}

		} catch (AppettitException e) {
			logger.error(e.getMessage().trim());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
		}

	}

	public void abrirRestaurante() throws AppettitException {
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
	
	public void logout() {
		usrSession.destroySession();
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
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
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
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
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
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
			return null;
		}
	}
	
	public StreamedContent getChartVentasTotales() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					CategoryDataset dataset = createDataset(sortByKey(ventas.getData()));
					
					
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
					plot.getRenderer().setSeriesPaint(0, new Color(242, 162, 44));
					
										
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
			return null;
		}
	}

	public StreamedContent getChartClientesTotales() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					CategoryDataset dataset = createDataset(sortByKey(clientes.getData()));
					
					
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
					plot.getRenderer().setSeriesPaint(0, new Color(242, 162, 44, 250));
					
										
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
			return null;
		}
	}

	public StreamedContent getChartOrdenesTotales() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					CategoryDataset dataset = createDataset(sortByKey(ordenes.getData()));
					
					
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
					plot.getRenderer().setSeriesPaint(0, new Color(242, 162, 44));
					
										
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
			return null;
		}
	}

	public StreamedContent getChartOrdenesPromedioTotales() {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					CategoryDataset dataset = createDataset(sortByKey(pedidosPromedio.getData()));
					
					
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
					plot.getRenderer().setSeriesPaint(0, new Color(242, 162, 44));
					
										
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
			return null;
		}
	}

	public StreamedContent getChartRapidezTotales() {
			return getChartCalificacionTotales(rapidez.getData());
	}
	
	public StreamedContent getChartComidaTotales() {
		return getChartCalificacionTotales(comida.getData());
	}
	
	public StreamedContent getChartServicioTotales() {
		return getChartCalificacionTotales(servicio.getData());
	}
	
	public StreamedContent getChartCalGralTotales() {
		try {
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					
					DefaultCategoryDataset dataset = new DefaultCategoryDataset();
					
					for (Map.Entry<String, Double> entry : rapidez.getData().entrySet()) {
						dataset.addValue(entry.getValue(), "rapidez", entry.getKey());
					}
					for (Map.Entry<String, Double> entry : comida.getData().entrySet()) {
						dataset.addValue(entry.getValue(), "comida", entry.getKey());
					}
					for (Map.Entry<String, Double> entry : servicio.getData().entrySet()) {
						dataset.addValue(entry.getValue(), "servicio", entry.getKey());
					}
					
					final JFreeChart chart = ChartFactory.createStackedBarChart(
				            "",      // chart title
				            "",                // domain axis label
				            "",                   // range axis label
				            (CategoryDataset) dataset,                   // data
				            PlotOrientation.HORIZONTAL,  // orientation
				            false,                      // include legend
				            true,
				            false
				        );
					
					final CategoryPlot plot = (CategoryPlot) chart.getPlot();
					plot.setForegroundAlpha(0.5f);
			        plot.setBackgroundPaint(Color.WHITE);
			        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
			        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
			        
			        plot.setRenderer( new CustomHorizontalStackedBarChartRenderer());
			        
					NumberAxis vn = (NumberAxis) plot.getRangeAxis();   
					vn.setTickUnit(new NumberTickUnit(1d)); 
										
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
			return null;
		}
	}
	
	private StreamedContent getChartCalificacionTotales(Map<String, Double> datos) {
		try {
			
			return DefaultStreamedContent.builder().contentType("image/png").writer((os) -> {
				try {
					CategoryDataset dataset = createDataset(sortByKey(datos));
					
					final JFreeChart chart = ChartFactory.createBarChart(
				            "",      // chart title
				            "",                // domain axis label
				            "",                   // range axis label
				            dataset,                   // data
				            PlotOrientation.HORIZONTAL,  // orientation
				            false,                      // include legend
				            true,
				            false
				        );
					
					final CategoryPlot plot = (CategoryPlot) chart.getPlot();
					plot.setForegroundAlpha(0.5f);
			        plot.setBackgroundPaint(Color.WHITE);
			        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
			        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
			        
			        plot.setRenderer( new CustomHorizontalBarChartRenderer());
			        
					NumberAxis vn = (NumberAxis) plot.getRangeAxis();   
					vn.setTickUnit(new NumberTickUnit(1d)); 
										
					ChartUtils.writeChartAsPNG(os, chart, 375, 300);
				} catch (Exception e) {
					logger.error(e.getMessage().trim());
				}
			}).build();
		} catch (Exception e) {
			logger.error(e.getMessage().trim());
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
			dataset.addValue(entry.getValue(), series, entry.getKey());
		}

		
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
	
	// function to sort hashmap by keys
	// https://www.geeksforgeeks.org/sorting-hashmap-according-key-value-java/
    public static Map<String, Double> sortByKey(Map<String, Double> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Double> > list = new LinkedList<Map.Entry<String, Double> >(hm.entrySet());
 
        // Sort the list using lambda expression
        Collections.sort(list,(i1, i2) -> i1.getKey().compareTo(i2.getKey()));
 
        // put data from sorted list to hashmap
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>();
        for (Map.Entry<String, Double> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
    
    
 	class CustomHorizontalBarChartRenderer extends BarRenderer{
		
		private static final long serialVersionUID = 1L;
		
		public java.awt.Paint getItemPaint(int row,int column){
			Color color = null;	
			
			if(column == 0) color = new Color(213,51,67);
			else if(column == 1) color = new Color(247,187,7);
	        else if(column == 2) color = new Color(23,162,184);
	        else if(column == 3) color = new Color(0,123,255);
	        else if(column == 4) color = new Color(40,167,69);
			else color = new Color(0,0,0);
			
			return color;
		}
	}
 	
	class CustomHorizontalStackedBarChartRenderer extends StackedBarRenderer {
		
		private static final long serialVersionUID = 1L;
		
		public java.awt.Paint getItemPaint(int row,int column){
			Color color = null;	
			
			if(column == 0) {
				if(row == 1) color = new Color(213,51,67, 150);
				else color = new Color(213,51,67);
			}
			else if(column == 1) {
				if(row == 1) color = new Color(247,187,7, 150);
				else  color = new Color(247,187,7);
			}
	        else if(column == 2) {
	        	if(row == 1) color = new Color(23,162,184, 150);
	        	else  color = new Color(23,162,184);
	        }
	        else if(column == 3) {
	        	if(row == 1) color = new Color(0,123,255, 150);
	        	else color = new Color(0,123,255);
	        }
	        else if(column == 4) {
	        	if (row == 1) color = new Color(40,167,69, 175);
	        	else  color = new Color(40,167,69);
	        }
			else color = new Color(0,0,0);
			
			return color;
		}
	}

}
