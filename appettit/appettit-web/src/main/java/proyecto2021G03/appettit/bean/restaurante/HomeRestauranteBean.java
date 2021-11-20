package proyecto2021G03.appettit.bean.restaurante;

import java.awt.Color;
import java.awt.Paint;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.RingPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.util.UnitType;
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
import proyecto2021G03.appettit.dto.DashTotalDTO;
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
	List<DashMenuDTO> tendencias;
	List<DashMenuDTO> recientes;
	DashTotalDTO formapago;
	Integer formapagoPedidos;
	Boolean abierto;
	String fechaHora;
	RestauranteDTO restauranteDTO;
	Long id_restaurante;
	FacesContext facesContext;
	HttpSession session;
	LocalDateTime fechaHasta = LocalDateTime.now();;
	LocalDateTime fechaDesde = fechaHasta.minusDays(7);; // Minu 0, 128, 55

	Paint[] fpColores = new Paint[] { new Color(0, 128, 55), new Color(242, 162, 44), };

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
			formapagoPedidos = 0;

			UsuarioDTO usuarioDTO = getUserSession();

			if (usuarioDTO == null) {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
			} else {
				restauranteDTO = usrService.buscarRestaurantePorId(usuarioDTO.getId());

				Date fechaBase = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				fechaHora = dateFormat.format(fechaBase);

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
					// JFreeChart jfreechart = ChartFactory.createPieChart("Cities",
					// createRingDataset(formapago), true, true, false);

					PieDataset dataset = createRingDataset(formapago.getData());

					JFreeChart chart = ChartFactory.createRingChart("", dataset, false, true, false);

					RingPlot pie = (RingPlot) chart.getPlot();

					pie.setBackgroundPaint(Color.WHITE);
					pie.setOutlineVisible(false);
					pie.setShadowPaint(null);

					for (int i = 0; i < dataset.getItemCount(); i++) {
						pie.setSectionPaint(dataset.getKey(i), fpColores[i]);
					}
					
					
			        pie.setBackgroundPaint(Color.WHITE);
			        pie.setOutlineVisible(false);
			        pie.setShadowPaint(null);
			        pie.setSimpleLabels(true);
			        pie.setLabelGenerator(new StandardPieSectionLabelGenerator("{1}"));
			        pie.setSimpleLabelOffset(new RectangleInsets(
			            UnitType.RELATIVE, 0.09, 0.09, 0.09, 0.09));
			        pie.setLabelBackgroundPaint(Color.WHITE);
			        pie.setLabelOutlinePaint(null);
			        pie.setLabelShadowPaint(null);
			        pie.setSectionDepth(0.33);
			        pie.setSectionOutlinesVisible(false);
			        pie.setSeparatorsVisible(false);
			        pie.setIgnoreZeroValues(true);
					

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
			//System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
			dataset.setValue(entry.getKey(), entry.getValue());
		}

		return dataset;
	}
}
