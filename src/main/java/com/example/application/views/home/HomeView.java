package com.example.application.views.home;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import com.example.application.controller.AgendaController;
import com.example.application.controller.OSController;
import com.example.application.controller.ProdutoController;
import com.example.application.model.Agenda;
import com.example.application.model.Cliente;
import com.example.application.model.ImagemOS;
import com.example.application.model.Produto;
import com.example.application.model.ProdutoOS;
import com.example.application.model.OrdemServico;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;


@PageTitle("Home")
@Route(value = "", layout = MainLayout.class)
public class HomeView extends Composite<VerticalLayout> {

        private AgendaController agendaController = new AgendaController();
        private OSController osController = new OSController();
        private ProdutoController produtoController = new ProdutoController();
        private Grid<Agenda> minimalistGrid4 = new Grid<>(Agenda.class, false);
        private Grid<OrdemServico> minimalistGrid5 = new Grid<>(OrdemServico.class, false);
        private Grid<Produto> minimalistGrid6 = new Grid<>(Produto.class, false);
        private VerticalLayout layoutColumn2 = new VerticalLayout();

        public HomeView() {
                layoutColumn2 = new VerticalLayout();
                H1 h12 = new H1();
                Hr hr4 = new Hr();
                H3 h34 = new H3();
                minimalistGrid4 = new Grid();
                minimalistGrid4.setAllRowsVisible(true);
                Hr hr5 = new Hr();
                H3 h35 = new H3();
                minimalistGrid5 = new Grid();
                minimalistGrid5.setAllRowsVisible(true);
                Hr hr6 = new Hr();
                H3 h36 = new H3();
                minimalistGrid6 = new Grid();
                minimalistGrid6.setAllRowsVisible(true);
                getContent().setWidth("100%");
                getContent().getStyle().set("flex-grow", "1");
                layoutColumn2.setWidthFull();
                getContent().setFlexGrow(1.0, layoutColumn2);
                layoutColumn2.setWidth("100%");
                layoutColumn2.getStyle().set("flex-grow", "1");
                h12.setText("Structura");
                h12.setWidth("max-content");
                
                h34.setText("Agendamentos para Hoje");
                h34.setWidth("max-content");
                minimalistGrid4.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_NO_ROW_BORDERS);
                minimalistGrid4.setWidth("100%");
                minimalistGrid4.getStyle().set("flex-grow", "0");

                h35.setText("OS para Hoje");
                h35.setWidth("max-content");
                minimalistGrid5.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_NO_ROW_BORDERS);
                minimalistGrid5.setWidth("100%");
                minimalistGrid5.getStyle().set("flex-grow", "0");

                h36.setText("Produtos com Estoque Baixo");
                h36.setWidth("max-content");
                minimalistGrid6.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_NO_BORDER,GridVariant.LUMO_NO_ROW_BORDERS);
                minimalistGrid6.setWidth("100%");
                minimalistGrid6.getStyle().set("flex-grow", "0");

                getContent().add(layoutColumn2);
                layoutColumn2.add(h12);
                layoutColumn2.add(hr4);
                layoutColumn2.add(h34);
                mudar();
                layoutColumn2.add(hr5);
                layoutColumn2.add(h35);
                mudar2();
                layoutColumn2.add(hr6);
                layoutColumn2.add(h36);
                mudar3();
        }

        private void mudar(){
                List<Agenda> listaDeAgendas = agendaController.pesquisarTarefasDeHoje();
                if (listaDeAgendas.isEmpty()) {
                        layoutColumn2.add(new Span("Nenhum agendamento encontrada para hoje."));
                        layoutColumn2.remove(minimalistGrid4);
                }else {
                        minimalistGrid4 =  createGrid();
                        layoutColumn2.add(minimalistGrid4);
                        minimalistGrid4.setItems(listaDeAgendas);
                }
        }

        private void mudar2(){
                List<OrdemServico> listaDeOrdemServicos = osController.OrdemServicoPorDataPrevista();
                if (listaDeOrdemServicos.isEmpty()) {
                        layoutColumn2.add(new Span("Nenhuma ordem de serviço encontrado para hoje."));
                        layoutColumn2.remove(minimalistGrid5);
                }else{
                        minimalistGrid5 =  createGrid2();
                        layoutColumn2.add(minimalistGrid5);
                        minimalistGrid5.setItems(listaDeOrdemServicos);
                }
        }

        private void mudar3(){
                List<Produto> listaDeProdutos = produtoController.pesquisarProdutoComQuantidadeMinimaIgual();
                if (listaDeProdutos.isEmpty()) {
                        layoutColumn2.add(new Span("Nenhum produto com baixo estoque."));
                        layoutColumn2.remove(minimalistGrid6);
                } else{
                        minimalistGrid6 =  createGrid3();
                        layoutColumn2.add(minimalistGrid6);
                        minimalistGrid6.setItems(listaDeProdutos);
                }
        }

        private Grid<Agenda> createGrid() {
                minimalistGrid4 = new Grid<>(Agenda.class, false);
                minimalistGrid4.setAllRowsVisible(true);
                minimalistGrid4.getStyle().set("border-radius", "15px");
                minimalistGrid4.getStyle().set("box-shadow", "0 0 1px rgba(0 , 0, 0, 0.1)");
                minimalistGrid4.setDetailsVisibleOnClick(false);
                minimalistGrid4.setItemDetailsRenderer(createAgendaDetailsRenderer());

                minimalistGrid4.addColumn(Agenda::getTitulo).setHeader("Titulo").setSortable(true);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                minimalistGrid4.addColumn(agenda -> agenda.getDataHora() != null ? agenda.getDataHora().format(formatter) : "")
                .setHeader("Data")
                .setSortable(true);

                minimalistGrid4.addColumn(new ComponentRenderer<>(agenda -> {
                Span statusBadge = new Span(agenda.getStatus() != null ? agenda.getStatus().getDescricao() : "Indefinido");
                if (agenda.getStatus() != null) {
                        switch (agenda.getStatus()) {
                        case ABERTA -> statusBadge.getStyle().set("background-color", "lightblue");
                        case EM_ANDAMENTO -> statusBadge.getStyle().set("background-color", "lightgoldenrodyellow");
                        case CONCLUIDA -> statusBadge.getStyle().set("background-color", "lightgreen");
                        case CANCELADA -> statusBadge.getStyle().set("background-color", "lightcoral");
                        default -> statusBadge.getStyle().set("background-color", "lightgray");
                        }
                }
                statusBadge.getStyle()
                        .set("color", "black")
                        .set("padding", "5px 10px")
                        .set("border-radius", "12px")
                        .set("font-weight", "bold");
                return statusBadge;
                })).setHeader("Status").setSortable(true);

                minimalistGrid4.addItemClickListener(event -> {
                Agenda agenda = event.getItem();
                minimalistGrid4.setDetailsVisible(agenda, !minimalistGrid4.isDetailsVisible(agenda));
                });

                return minimalistGrid4;
        }
        
        private static ComponentRenderer<HorizontalLayout, Agenda> createAgendaDetailsRenderer() {
                return new ComponentRenderer<>(agenda -> {
                HorizontalLayout detailsLayout = new HorizontalLayout();
                detailsLayout.setSpacing(true);
                detailsLayout.setPadding(true);
                detailsLayout.addClassName("details-layout");
        
                TextArea descricaoArea = new TextArea("Descrição");
                descricaoArea.setValue(agenda.getDescricao());
                descricaoArea.setReadOnly(true);
                descricaoArea.addClassName("rounded-text-field");

                TextArea enderecoArea = new TextArea("Endereço");
                enderecoArea.setValue(agenda.getEndereco());
                enderecoArea.setReadOnly(true);
                enderecoArea.addClassName("rounded-text-field");
        
                TextField funcionarioField = new TextField("Funcionario");
                funcionarioField.setValue(agenda.getFuncionario() != null ? agenda.getFuncionario().getNome() : "Indefinido");
                funcionarioField.setReadOnly(true);
                funcionarioField.addClassName("rounded-text-field");
        
                detailsLayout.add(funcionarioField, descricaoArea, enderecoArea);
                return detailsLayout;
                });
        }

        private Grid<OrdemServico> createGrid2() {
                minimalistGrid5 = new Grid<>(OrdemServico.class, false);
                minimalistGrid5.setAllRowsVisible(true);
                minimalistGrid5.getStyle().set("border-radius", "15px");
                minimalistGrid5.getStyle().set("box-shadow", "0 0 1px rgba(0 , 0, 0, 0.1)");
                
                minimalistGrid5.addColumn(OrdemServico::getId).setHeader("Número OS").setSortable(true).setWidth("150px").setFlexGrow(0);
                minimalistGrid5.addColumn(ordemServico -> ordemServico.getCliente().getNome()).setHeader("Cliente");
                minimalistGrid5.addColumn(OrdemServico::getDataAbertura).setHeader("Data Abertura");
                minimalistGrid5.addColumn(OrdemServico::getDataPrevFinaliza).setHeader("Data de Previsão");

                minimalistGrid5.addColumn(new ComponentRenderer<>(ordemServico -> {
                Span statusBadge = new Span(ordemServico.getStatusOS() != null ? ordemServico.getStatusOS().getDescricao() : "Indefinido");               
                if (ordemServico.getStatusOS() != null) {
                        switch (ordemServico.getStatusOS()) {
                        case ABERTA -> statusBadge.getStyle().set("background-color", "lightblue");
                        case EM_ANDAMENTO -> statusBadge.getStyle().set("background-color", "lightgoldenrodyellow");
                        case CONCLUIDA -> statusBadge.getStyle().set("background-color", "lightgreen");
                        case CANCELADA -> statusBadge.getStyle().set("background-color", "lightcoral");
                        default -> statusBadge.getStyle().set("background-color", "lightgray");
                        }
                }
                statusBadge.getStyle()
                        .set("color", "black")
                        .set("padding", "5px 10px")
                        .set("border-radius", "12px")
                        .set("font-weight", "bold");
                
                return statusBadge;
                })).setHeader("Status");

                minimalistGrid5.addComponentColumn(ordemServico -> {
                        Button detalhes = new Button();
                        detalhes.setIcon(VaadinIcon.EYE.create());
                        detalhes.addThemeVariants(ButtonVariant.LUMO_ICON);
                        detalhes.getStyle()
                                .set("border-radius", "50%")
                                .set("width", "40px")
                                .set("height", "40px")
                                .set("padding", "0")
                                .set("background-color", "var(--lumo-primary-color)")
                                .set("color", "white");
                        
                        detalhes.addClickListener(e -> {
                                Dialog dialog = new Dialog();
                                dialog.setHeaderTitle("Detalhes da OS #" + ordemServico.getId());
                                dialog.setWidth("600px");
                                dialog.setHeight("500px");
                        
                                VerticalLayout content = new VerticalLayout();
                                content.setPadding(true);
                                content.setSpacing(false);
                        
                                content.add(new Hr());
                                content.add(new Paragraph("Endereço: " + (ordemServico.getEndereco() != null ? ordemServico.getEndereco() : "")));
                                content.add(new Paragraph("Entrega: " + (ordemServico.getEntregaOS().getDescricao())));
                                content.add(new Paragraph("Observação: " + (ordemServico.getObservacao() != null ? ordemServico.getObservacao() : "")));
                                content.add(new Paragraph("Funcionário: " + (ordemServico.getFuncionario().getNome())));
                        
                                dialog.add(content);
                                dialog.open();
                        });
                        return detalhes;
                }).setHeader("Detalhes");
                return minimalistGrid5;
        }

        private Grid<Produto> createGrid3() {
                minimalistGrid6 = new Grid<>(Produto.class, false);
                minimalistGrid6.setAllRowsVisible(true);
                minimalistGrid6.getStyle().set("border-radius", "15px");
                minimalistGrid6.getStyle().set("box-shadow", "0 0 1px rgba(0 , 0, 0, 0.1)");
                minimalistGrid6.setDetailsVisibleOnClick(false);
                minimalistGrid6.setItemDetailsRenderer(createProdutoDetailsRenderer());

                minimalistGrid6.addColumn(Produto::getNome).setHeader("Nome").setSortable(true);
                minimalistGrid6.addColumn(produto -> produto.getMaterial() != null && produto.getMaterial().getNome() != null
                        ? produto.getMaterial().getNome()
                        : "Sem Material").setHeader("Material").setSortable(true);
                minimalistGrid6.addColumn(produto -> produto.getModelo() != null && produto.getModelo().getNome() != null
                        ? produto.getModelo().getNome()
                        : "Sem Modelo").setHeader("Modelo").setSortable(true);

                minimalistGrid6.addItemClickListener(event -> {
                        Produto produto = event.getItem();
                        minimalistGrid6.setDetailsVisible(produto, !minimalistGrid6.isDetailsVisible(produto));
                });

                return minimalistGrid6;
        }

        private static ComponentRenderer<HorizontalLayout, Produto> createProdutoDetailsRenderer() {
                return new ComponentRenderer<>(produto -> {
                HorizontalLayout detailsLayout = new HorizontalLayout();
                detailsLayout.setSpacing(true);
                detailsLayout.setPadding(true);
                detailsLayout.addClassName("details-layout");

                TextField unidMedidaField = new TextField("Unidade de Medida");
                String unidMedidaValue = produto.getUnidMedida() != null && produto.getUnidMedida().getNome() != null
                        ? produto.getUnidMedida().getNome()
                        : "Sem Unidade de Medida";
                unidMedidaField.setValue(unidMedidaValue);
                unidMedidaField.setReadOnly(true);
                unidMedidaField.addClassName("rounded-text-field");

                TextField quantidadeAtualField = new TextField("Quantidade Atual");
                quantidadeAtualField.setValue(String.valueOf(produto.getQuantidadeAtual()));
                quantidadeAtualField.setReadOnly(true);
                quantidadeAtualField.addClassName("rounded-text-field");

                TextField quantidadeMinimaField = new TextField("Quantidade Mínima");
                quantidadeMinimaField.setValue(String.valueOf(produto.getQuantidadeMinima()));
                quantidadeMinimaField.setReadOnly(true);
                quantidadeMinimaField.addClassName("rounded-text-field");

                NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                String custoFormatado = currencyFormat.format(produto.getCustoUnitario());

                TextField custoUnitarioField = new TextField("Custo Unitário");
                custoUnitarioField.setValue(custoFormatado);
                custoUnitarioField.setReadOnly(true);
                custoUnitarioField.addClassName("rounded-text-field");

                detailsLayout.add(unidMedidaField, quantidadeAtualField, quantidadeMinimaField, custoUnitarioField);
                return detailsLayout;
                });
        }
}