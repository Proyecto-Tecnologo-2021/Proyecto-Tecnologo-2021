package proyecto2021G03.appettit.bean.admin;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.imageio.ImageIO;
import javax.inject.Named;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.RingPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.primefaces.component.export.PDFOptions;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import proyecto2021G03.appettit.bean.user.IUserSession;
import proyecto2021G03.appettit.business.IEstadisticasService;
import proyecto2021G03.appettit.business.IMenuService;
import proyecto2021G03.appettit.business.IUsuarioService;
import proyecto2021G03.appettit.dto.AdministradorDTO;
import proyecto2021G03.appettit.dto.DashInformeDTO;
import proyecto2021G03.appettit.dto.UsuarioDTO;
import proyecto2021G03.appettit.util.Constantes;

@Named("beanAdminInformes")
//@RequestScoped
@SessionScoped
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InformesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	static Logger logger = Logger.getLogger(InformesBean.class);

	List<DashInformeDTO> contenido;
	List<DashInformeDTO> selectedProducts;
	private String selectedOption;
	private List<LocalDate> range;
	private PDFOptions pdfOpt;
	private String tinforme;
	private String nominforme;
	String fechaHora;
	AdministradorDTO administradorDTO;
	Long id_restaurante;
	FacesContext facesContext;
	HttpSession session;
	LocalDateTime fechaHasta = LocalDateTime.now();;
	LocalDateTime fechaDesde = fechaHasta.minusDays(7);

	@EJB
	IUsuarioService usrSrv;

	@EJB
	IMenuService menuSRV;

	@EJB
	IUsuarioService usrService;

	@EJB
	IEstadisticasService estadisitciasSrv;

	@EJB
	IUserSession usrSession;

	@PostConstruct
	public void init() {
		facesContext = FacesContext.getCurrentInstance();
		//ExternalContext externalContext = facesContext.getExternalContext();
		session = (HttpSession) facesContext.getExternalContext().getSession(true);

		UsuarioDTO usuarioDTO = getUserSession();

		if (usuarioDTO == null) {
			//externalContext.invalidateSession();
			//externalContext.dispatch(Constantes.REDIRECT_URI);
			//externalContext.redirect(Constantes.REDIRECT_URI);
			usrSession.destroySession();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));
		} else {
			if (!(usuarioDTO instanceof AdministradorDTO)) {
				//externalContext.invalidateSession();
				//externalContext.dispatch(Constantes.REDIRECT_URI);
				//externalContext.redirect(Constantes.REDIRECT_URI);
				usrSession.destroySession();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, "USUARIO NO LOGUEADO", null));

			} else {
				customizationOptions();

				Date fechaBase = new Date();
				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				fechaHora = dateFormat.format(fechaBase);
				tinforme = "";

			}
		}
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

	public void customizationOptions() {
		pdfOpt = new PDFOptions();
		pdfOpt.setFacetBgColor("#f2a22c");
		pdfOpt.setFacetFontColor("#ffffff");
		pdfOpt.setFacetFontStyle("BOLD");
		pdfOpt.setCellFontSize("8");
		pdfOpt.setFontName("Verdana");
	}

	public void listarInforme() {
		if (!(selectedOption == null || range.get(0) == null || range.get(1) == null)) {
			try {
				if (selectedOption.equalsIgnoreCase("Option1")) {
					this.contenido = estadisitciasSrv.listarInfoVentasPorFecha(range.get(0).atStartOfDay(), range.get(1).atStartOfDay());
					tinforme = "Ventas diarias totales";
				} else if (selectedOption.equalsIgnoreCase("Option2")) {
					this.contenido = estadisitciasSrv.listarInfoVentasPorFechaRestaurante(range.get(0).atStartOfDay(), range.get(1).atStartOfDay());
					tinforme = "Ventas diarias por restaurante totales";
				} else if (selectedOption.equalsIgnoreCase("Option3")) {
					this.contenido = estadisitciasSrv.listarInfoVentasPorFechaBarrio(range.get(0).atStartOfDay(), range.get(1).atStartOfDay());
					tinforme = "Ventas por Departamento barrio totales";
				}

				nominforme = tinforme + "_" + getFechaHora(range.get(0).atStartOfDay(), "yyymmdd") +"-" + getFechaHora(range.get(1).atStartOfDay(), "yyymmdd");
			} catch (Exception e) {
				logger.error(e.getMessage().trim());
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage().trim(), null));
			}
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked", "static-access", "unused" })
	public void exportarInforme() {
		if (!(selectedOption == null || range.get(0) == null || range.get(1) == null)) {
			
			nominforme = tinforme + "_" + getFechaHora(range.get(0).atStartOfDay(), "yyymmdd") +"-" + getFechaHora(range.get(1).atStartOfDay(), "yyymmdd");
			
			
			Document document = new Document(PageSize.A4);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			Date hoy = new Date();
			try {

				DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				String strDate = dateFormat.format(hoy);

				PdfWriter writer = PdfWriter.getInstance(document, baos);
				document.open();
				Font fuente1 = new Font();
				Font fuente2 = new Font();
				fuente1.setSize(20);
				fuente2.setSize(10);

				document.add(new Paragraph("Appetit \n", fuente1));
				document.add(new Paragraph("\n", fuente2));
				document.add(new Paragraph("Fecha: " + strDate + "\n", fuente2));
				document.add(new Paragraph("\n", fuente2));
				document.add(new Paragraph(tinforme + "\n", fuente2));

				LineSeparator linea = new LineSeparator();
				linea.setOffset(-8.0f);
				linea.setAlignment(Element.ALIGN_LEFT);
				linea.setPercentage(100.0f);
				document.add(linea);

				Font font = FontFactory.getFont(FontFactory.HELVETICA);
				font.setSize(10);
				font.setColor(CMYKColor.WHITE);
				
				Font font2 = FontFactory.getFont(FontFactory.HELVETICA);
				font2.setSize(10);
				font2.setColor(CMYKColor.BLACK);

				// define table header cell
				PdfPCell cell = new PdfPCell();
				PdfPCell cell2 = new PdfPCell();
				cell.setBackgroundColor(new BaseColor(242, 162, 44));
				cell.setPadding(5);

				PdfPTable table2 = new PdfPTable(5);
				table2.setWidthPercentage(100.0f);
				table2.setWidths(new float[] { 2.0f, 3.0f, 3.0f, 2.0f, 2.0f });
				table2.setSpacingBefore(10);

				// write table header
				cell.setPhrase(new Phrase("Período", font));
				table2.addCell(cell);

				cell.setPhrase(new Phrase("Detalle", font));
				table2.addCell(cell);

				cell.setPhrase(new Phrase("Detalle 2", font));
				table2.addCell(cell);

				cell.setPhrase(new Phrase("Total", font));
				table2.addCell(cell);

				cell.setPhrase(new Phrase("Acumulado", font));
				table2.addCell(cell);
				
				DefaultCategoryDataset dataset = new DefaultCategoryDataset();
				DefaultPieDataset datasetp = new DefaultPieDataset();

				for (DashInformeDTO f : this.contenido) {
					
					if (selectedOption.equalsIgnoreCase("Option1")) {
						dataset.addValue(f.getTotal(), "ventas", f.getPeriodo());
					} else if (selectedOption.equalsIgnoreCase("Option2")) {
						dataset.addValue(f.getTotal(), f.getDetalle(), f.getPeriodo());
					} else if (selectedOption.equalsIgnoreCase("Option3")) {
						datasetp.setValue(f.getDetalleB(), f.getTotal());
					}
					
					
					cell2.setPhrase(new Phrase(f.getPeriodo(), font2));
					cell2.setHorizontalAlignment(cell2.ALIGN_LEFT);
					table2.addCell(cell2);

					cell2.setPhrase(new Phrase(f.getDetalle(), font2));
					cell2.setHorizontalAlignment(cell2.ALIGN_LEFT);
					table2.addCell(cell2);

					cell2.setPhrase(new Phrase(f.getDetalleB(), font2));
					cell2.setHorizontalAlignment(cell2.ALIGN_LEFT);
					table2.addCell(cell2);

					cell2.setPhrase(new Phrase(String.valueOf(f.getTotal()), font2));
					cell2.setHorizontalAlignment(cell2.ALIGN_RIGHT);
					table2.addCell(cell2);

					cell2.setPhrase(new Phrase(String.valueOf(f.getTotalB()), font2));
					cell2.setHorizontalAlignment(cell2.ALIGN_RIGHT);
					table2.addCell(cell2);

				}

				// Create chart
				JFreeChart chart = null;
				if (selectedOption.equalsIgnoreCase("Option1")) {
					chart = ChartFactory.createStackedAreaChart(
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
					
				} else if (selectedOption.equalsIgnoreCase("Option2")) {
					chart = ChartFactory.createBarChart(
				            "",      // chart title
				            "",                // domain axis label
				            "",                   // range axis label
				            dataset,                   // data
				            PlotOrientation.VERTICAL,  // orientation
				            true,                      // include legend
				            true,
				            false
				        );
					
					final CategoryPlot plot = (CategoryPlot) chart.getPlot();
					plot.setForegroundAlpha(0.5f);
			        plot.setBackgroundPaint(Color.WHITE);
			        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
			        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
				
				} else if (selectedOption.equalsIgnoreCase("Option3")) {
					chart = ChartFactory.createRingChart("", datasetp, true, true, false);
					RingPlot pie = (RingPlot) chart.getPlot();

					pie.setBackgroundPaint(Color.WHITE);
					pie.setOutlineVisible(false);
					pie.setLabelGenerator(null);
					pie.setSectionDepth(0.33);
					pie.setSectionOutlinesVisible(false);
					pie.setShadowPaint(null);
				}
				
				if(chart!=null) {
					BufferedImage bufferedImage = chart.createBufferedImage(500, 300);
					ByteArrayOutputStream baosi = new ByteArrayOutputStream();
					ImageIO.write(bufferedImage, "png", baosi);
					Image iTextImage = Image.getInstance(baosi.toByteArray());
					
					LineSeparator linea1 = new LineSeparator();
					linea1.setOffset(-8.0f);
					linea1.setPercentage(100.0f);

					Paragraph p = new Paragraph();
					p.add(linea1);
					document.add(p);

					document.add(iTextImage);

					LineSeparator linea2 = new LineSeparator();
					linea2.setOffset(-8.0f);
					linea2.setPercentage(100.0f);
					document.add(linea2);
					
				}

				document.add(table2);

				LineSeparator linea3 = new LineSeparator();
				linea3.setOffset(-8.0f);
				linea3.setPercentage(100.0f);
				document.add(linea3);

			} catch (Exception e) {
				logger.error("Error imprimirPDF: " + e.getMessage());
			}

			document.close();

			FacesContext context = FacesContext.getCurrentInstance();
			Object response = context.getExternalContext().getResponse();
			if (response instanceof HttpServletResponse) {
				HttpServletResponse hsr = (HttpServletResponse) response;
				hsr.setContentType("application/pdf");
				hsr.setHeader("Content-disposition", "attachment;filename=/" + nominforme + ".pdf");
				hsr.setContentLength(baos.size());
				try {
					ServletOutputStream out = hsr.getOutputStream();
					baos.writeTo(out);
					out.flush();
				} catch (IOException ex) {
					System.out.println("Error:  " + ex.getMessage());
				}
				context.responseComplete();
			}

			
		}

	}

}
