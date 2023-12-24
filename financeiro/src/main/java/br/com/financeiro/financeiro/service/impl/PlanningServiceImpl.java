package br.com.financeiro.financeiro.service.impl;

import br.com.financeiro.financeiro.domain.Categories;
import br.com.financeiro.financeiro.domain.Planning;
import br.com.financeiro.financeiro.domain.User;
import br.com.financeiro.financeiro.exception.BadRequestException;
import br.com.financeiro.financeiro.record.PlanningRecord;
import br.com.financeiro.financeiro.repository.CategoryDefaultRepository;
import br.com.financeiro.financeiro.repository.CategoryRepository;
import br.com.financeiro.financeiro.repository.PlanningRepository;
import br.com.financeiro.financeiro.repository.UserRepository;
import br.com.financeiro.financeiro.service.PlanningService;
import br.com.financeiro.financeiro.service.mapper.PlanningMapper;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.DashedBorder;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

/**
 * Service Implementation for managing {@link Planning}.
 */
@Service
public class PlanningServiceImpl implements PlanningService {

    @Autowired
    PlanningRepository planningRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryDefaultRepository categoryDefaultRepository;

    @Autowired
    PlanningMapper planningMapper;

    @Autowired
    UserRepository userRepository;


    @Override
    @Transactional
    public Planning save(Planning planning) { return planningRepository.save(planning); }

    @Override
    public Optional<PlanningRecord> findOne(UUID id) { return planningRepository.findById(id).map(planningMapper::toDto); }

    @Override
    public void delete(UUID id) { planningRepository.deleteById(id); }

    @Override
    @Transactional
    public PlanningRecord savePlanning(PlanningRecord planningRecord, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: Usuário não foi encontrada"));

        Optional<PlanningRecord> onePlanningByMonthAndYear = findOnePlanningByMonthAndYear(planningRecord.month(), planningRecord.year(), user.getId());
        if (onePlanningByMonthAndYear.isPresent()) {
            throw new BadRequestException("Já existe um planejamento cadastrado para este mês e ano");
        }

        Planning planning = planningMapper.toEntity(planningRecord);

        if (planning.getCategories() != null) {
            planning.getCategories().forEach(categorie -> {

                if (categorie.getCategory().getId() != null && categoryDefaultRepository.existsById(categorie.getCategory().getId())) {
                    categorie.setPlanning(planning);
                }
            });
        }

        if(planning.getUserList() == null) {
            planning.setUserList(new ArrayList<>());
        }
        planning.getUserList().add(user);


        Planning result = save(planning);
        return planningMapper.toDto(result);

    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PlanningRecord> findOnePlanningByMonthAndYear(Integer month, Integer year, UUID userId) {

        Optional<Planning> byMonthAndYearAndUserListId = planningRepository.findByMonthAndYearAndUserList_Id(month, year, userId);

        return byMonthAndYearAndUserListId.map(planningMapper::toDto);
    }

    @Override
    @Transactional
    public void downloadPlanning(HttpServletResponse response, UUID planningId, UUID userId) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Error: Usuário não foi encontrada"));

        List<Planning> planningList = user.getPlanningList();
        Optional<Planning> findPlanning = planningList.stream()
                .filter(planning -> planning.getId().equals(planningId))
                .findFirst();

        if (findPlanning.isPresent()) {
            Planning planning = findPlanning.get();

            monipulatePDF(response, user, planning);

        } else {
            throw new RuntimeException("Planejamento não encontrado com o ID: " + planningId);
        }
    }

    private void monipulatePDF(HttpServletResponse response, User user, Planning planning) throws IOException {

        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataAtualFormatada = dataAtual.format(formatter);

        Month mes = Month.of(planning.getMonth());
        String nomeDoMes = mes.getDisplayName(TextStyle.FULL, Locale.getDefault());
        String dataPlanejamento = nomeDoMes+"/"+planning.getYear();

        String CPF = user.getCpf().replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
        String telefone = user.getPhoneNumber().replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
        String salario = formatarSalario(planning.getTotalPlanned());


        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=planejamento_" + dataPlanejamento + ".pdf");
        PdfWriter pdfWriter = new PdfWriter(response.getOutputStream());
        PdfDocument pdfDocument = new PdfDocument(pdfWriter);
        pdfDocument.setDefaultPageSize(PageSize.A4);
        //Adiciona marca dagua
        pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, new WatermarkingEventHandler());

        Document document = new Document(pdfDocument);

        float tresCores = 190f;
        float colunaDireita = 385f;
        float colunaEsquerda = colunaDireita + 185f;
        float tamanhoTotal[] = {colunaEsquerda, colunaDireita};
        float larguraCompleta[] = {tresCores*3};
        float colunasDaTabela[] = {tresCores, tresCores, tresCores, tresCores, tresCores};
        Paragraph quebraLinha = new Paragraph("\n");

        //Cabecalho
        Table cabecalho = new Table(tamanhoTotal);

        Table colunaDireitaDoisCampos = new Table(new float[] {colunaDireita/2, colunaDireita/2});
        colunaDireitaDoisCampos.addCell(obterTextoTratado("Data Planejamento:"));
        colunaDireitaDoisCampos.addCell(obterValorDoTextoTratado(dataPlanejamento));
        colunaDireitaDoisCampos.addCell(obterTextoTratado("Data PDF:"));
        colunaDireitaDoisCampos.addCell(obterValorDoTextoTratado(dataAtualFormatada));


        cabecalho.addCell(new Cell().add(new Paragraph("IF - Inteligência Financeira")).setFontSize(20f).setBorder(Border.NO_BORDER).setBold());
        cabecalho.addCell(new Cell().add(colunaDireitaDoisCampos).setBorder(Border.NO_BORDER));

        Border borda = new SolidBorder(new DeviceRgb(169, 169, 169), 2f);
        Table divisoria = new Table(larguraCompleta);
        divisoria.setBorder(borda);

        document.add(cabecalho);
        document.add(quebraLinha);
        document.add(divisoria);
        document.add(quebraLinha);
        //Cabecalho

        //Informacoes
        Table bodyTituloInformacoesPessoais = new Table(tamanhoTotal);
        bodyTituloInformacoesPessoais.addCell(obterDadosPessoais("Informações Pessoais"));
        bodyTituloInformacoesPessoais.addCell(obterDadosPessoais("Informações Adicionais"));
        document.add(bodyTituloInformacoesPessoais.setMarginBottom(12f));

        Table bodyInformacoesPessoais = new Table(tamanhoTotal);
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("Nome:", true));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("Salário:", true));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda(user.getName(), false));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda(salario, false));

        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("CPF:", true));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("", true));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda(CPF, false));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("", false));

        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("Telefone:", true));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("", true));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda(telefone, false));
        bodyInformacoesPessoais.addCell(obterCellParaEsquerda("", false));
        document.add(bodyInformacoesPessoais);
        //Informacoes

        Border borda2 = new DashedBorder(new DeviceRgb(169, 169, 169), 0.5f);
        divisoria.setBorder(borda2);
        document.add(quebraLinha);
        document.add(divisoria);

        //Categorias
        Paragraph categorias = new Paragraph("Categorias");
        document.add(categorias.setBold().setFontSize(14f));

        Table tabela = new Table(colunasDaTabela);
        tabela.setBackgroundColor(DeviceRgb.BLACK, 0.7f);
        tabela.addCell(new Cell().add(new Paragraph("Categoria")).setBold().setFontColor(DeviceRgb.WHITE).setBorder(Border.NO_BORDER));
        tabela.addCell(new Cell().add(new Paragraph("Tipo")).setBold().setFontColor(DeviceRgb.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        tabela.addCell(new Cell().add(new Paragraph("Valor")).setBold().setFontColor(DeviceRgb.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        tabela.addCell(new Cell().add(new Paragraph("Despesas Pagas")).setBold().setFontColor(DeviceRgb.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        tabela.addCell(new Cell().add(new Paragraph("Descrição")).setBold().setFontColor(DeviceRgb.WHITE).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        document.add(tabela);


        Table dadosDaTabela = new Table(colunasDaTabela);
        BigDecimal totalSoma = BigDecimal.ZERO;
        for (Categories categories : planning.getCategories()) {

            BigDecimal valorPlanned = categories.getPlanned();
            totalSoma = totalSoma.add(valorPlanned);

            dadosDaTabela.addCell(new Cell().add(new Paragraph(categories.getCategory().getName())).setBorder(Border.NO_BORDER));
            dadosDaTabela.addCell(new Cell().add(new Paragraph(categories.getCategory().getTypeCategory().toString())).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            dadosDaTabela.addCell(new Cell().add(new Paragraph(formatarSalario(categories.getPlanned()))).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT));
            dadosDaTabela.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            dadosDaTabela.addCell(new Cell().add(new Paragraph(categories.getDescricao() != null ? categories.getDescricao() : "")).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
        }
        document.add(dadosDaTabela.setMarginBottom(20f).setFontSize(8f));

        Table valorTotal = new Table(colunasDaTabela);
        valorTotal.addCell(new Cell().add(new Paragraph("Total")).setTextAlignment(TextAlignment.LEFT).setBorder(Border.NO_BORDER));
        valorTotal.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        valorTotal.addCell(new Cell().add(new Paragraph(formatarSalario(totalSoma))).setTextAlignment(TextAlignment.CENTER).setBorder(Border.NO_BORDER));
        valorTotal.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
        valorTotal.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

        document.add(valorTotal);
        //Categorias

        document.add(divisoria);
        document.add(quebraLinha);
        Border borda3 = new SolidBorder(new DeviceRgb(169, 169, 169), 1f);
        divisoria.setBorder(borda3);
        document.add(divisoria.setMarginBottom(15f));

        //Dicas
        Table dicas = new Table(colunasDaTabela);
        dicas.addCell(new Cell().add(new Paragraph("DICAS\n")).setBold().setBorder(Border.NO_BORDER));
        dicas.addCell(new Cell().add(new Paragraph("")).setBold().setBorder(Border.NO_BORDER));
        dicas.addCell(new Cell().add(new Paragraph("")).setBold().setBorder(Border.NO_BORDER));
        dicas.addCell(new Cell().add(new Paragraph("")).setBold().setBorder(Border.NO_BORDER));
        dicas.addCell(new Cell().add(new Paragraph("")).setBold().setBorder(Border.NO_BORDER));
        Paragraph dica1 = new Paragraph("1. Pense antes de fazer compras impulsivas. Avalie se o item é realmente necessário");
        Paragraph dica2 = new Paragraph("2. Considere seu estilo de vida atual e futuras mudanças ao criar seu planejamento financeiro");
        Paragraph dica3 = new Paragraph("3. Priorize o pagamento de dívidas com juros mais altos para economizar dinheiro a longo prazo");
        Paragraph dica4 = new Paragraph("4. Defina metas financeiras alcançáveis e realistas para manter o planejamento sustentável.");

        document.add(dicas);
        document.add(quebraLinha);
        document.add(dica1.setFontSize(12f));
        document.add(dica2.setFontSize(12f));
        document.add(dica3.setFontSize(12f));
        document.add(dica4.setFontSize(12f));
        //Dicas

        document.close();
    }

    private Cell obterTextoTratado(String texto) {
        return  new Cell().add(new Paragraph(texto)).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    private Cell obterValorDoTextoTratado(String texto) {
        return new Cell().add(new Paragraph(texto)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    private Cell obterDadosPessoais(String texto) {
        return new Cell().add(new Paragraph(texto)).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    private Cell obterCellParaEsquerda(String texto, Boolean isBold) {
        Cell cell = new Cell().add(new Paragraph(texto)).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold ? cell.setBold() : cell;
    }

    public static String formatarSalario(BigDecimal salario) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.getDefault());
        symbols.setCurrencySymbol("R$");
        symbols.setGroupingSeparator('.');
        symbols.setMonetaryDecimalSeparator(',');

        DecimalFormat formato = new DecimalFormat("¤ #,##0.00", symbols);

        // Formata o valor de salário
        return formato.format(salario);
    }

    private static class WatermarkingEventHandler implements IEventHandler {
        @Override
        public void handleEvent(Event currentEvent) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) currentEvent;
            PdfDocument pdfDoc = docEvent.getDocument();
            PdfPage page = docEvent.getPage();
            PdfFont font = null;
            try {
                font = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            } catch (IOException e) {

                // Such an exception isn't expected to occur,
                // because helvetica is one of standard fonts
                System.err.println(e.getMessage());
            }

            PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
            new Canvas(canvas, page.getPageSize())
                    .setFontColor(ColorConstants.LIGHT_GRAY, 0.7f)
                    .setFontSize(60)

                    // If the exception has been thrown, the font variable is not initialized.
                    // Therefore null will be set and iText will use the default font - Helvetica
                    .setFont(font)
                    .showTextAligned(new Paragraph("Inteligência Financeira"), 298, 421, pdfDoc.getPageNumber(page),
                            TextAlignment.CENTER, VerticalAlignment.MIDDLE, 45)
                    .close();
        }
    }
}
