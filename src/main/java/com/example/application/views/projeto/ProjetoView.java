package com.example.application.views.projeto;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.example.application.controller.ClienteController;
import com.example.application.controller.ProjetoController;
import com.example.application.model.Cliente;
import com.example.application.model.StatusProjeto;
import com.example.application.model.Projeto;
import com.example.application.model.StatusAgenda;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.component.datepicker.DatePicker;

@PageTitle("Projeto")
@Route(value = "my-view7", layout = MainLayout.class)
public class ProjetoView extends Composite<VerticalLayout> {

    private ProjetoController projetoController = new ProjetoController();
    private ClienteController clienteController = new ClienteController();
    private Grid<Projeto> grid = new Grid(Projeto.class, false);
    private ComboBox<Cliente> cliente = new ComboBox<>();
    private ComboBox<StatusProjeto> status = new ComboBox<>();
    private TextField medidas = new TextField();
    private TextArea descricao = new TextArea();
    private TextField valor = new TextField();
    private TextField desconto = new TextField();
    private TextField valorfinal = new TextField();
    private DatePicker data = new DatePicker();
    private Long projetoId;
    private TabSheet tabSheet;

    public ProjetoView() {
        tabSheet = new TabSheet();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        getContent().add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div projetosContent = createProjetosContent();
        tabSheet.add("Projetos", projetosContent);
        Div addProjetosContent = createAddProjetosContent();
        tabSheet.add("Adicionar Projeto", addProjetosContent);
    }

    private Div createProjetosContent(){
        Div projetosContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();
        textField.setPlaceholder("Cliente, Status ou Data");
        textField.setWidth("250px");
        layoutRow.setWidthFull();
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setAlignItems(Alignment.END);
        layoutRow.setJustifyContentMode(JustifyContentMode.END);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("70px");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.setIcon(VaadinIcon.SEARCH.create());
        buttonPrimary.getStyle().set("border-radius", "50%");
        textField.addClassName("rounded-text-field");
        layoutRow.add(textField, buttonPrimary);
        grid.setAllRowsVisible(true);
        grid.addClassName("borderless-grid");

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<Projeto> resultados;
            if(pesquisa.isEmpty()){
                resultados = projetoController.pesquisarTodos();
            }else{
                resultados = projetoController.pesquisarProjeto(pesquisa);
            }
            grid.setItems(resultados);
        });

        grid.addColumn(Projeto::getId).setHeader("Número").setWidth("150px").setFlexGrow(0);
        grid.addColumn(projeto -> {
            Cliente cliente = projeto.getCliente();
            return cliente != null ? cliente.getNome() : "Cliente não definido";
        }).setHeader("Cliente").setSortable(true);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        grid.addColumn(projeto -> projeto.getData() != null ? projeto.getData().format(formatter) : "").setHeader("Data").setSortable(true);

        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createPersonDetailsRenderer());

        grid.addColumn(new ComponentRenderer<>(projeto -> {
            Span statusBadge = new Span(projeto.getStatusPr() != null ? projeto.getStatusPr().getDescricao() : "Indefinido");
            if (projeto.getStatusPr() != null) {
                switch (projeto.getStatusPr()) {
                    case NOVO -> statusBadge.getStyle().set("background-color", "lightblue");
                    case EM_ANALISE -> statusBadge.getStyle().set("background-color", "lightgoldenrodyellow");
                    case APROVADO -> statusBadge.getStyle().set("background-color", "lightgreen");
                    case EM_EXECUCAO -> statusBadge.getStyle().set("background-color", "lightskyblue");
                    case PAUSADO -> statusBadge.getStyle().set("background-color", "lightgoldenrodyellow");
                    case CANCELADO -> statusBadge.getStyle().set("background-color", "lightcoral");
                    case CONCLUIDO -> statusBadge.getStyle().set("background-color", "limegreen");
                    default -> statusBadge.getStyle().set("background-color", "lightgray");
                }
            }
            statusBadge.getStyle()
            .set("color", "black")
            .set("padding", "5px 10px")
            .set("border-radius", "12px")
            .set("font-weight", "bold");
            return statusBadge;
        })).setHeader("Status").setAutoWidth(true);

        grid.addComponentColumn(projeto -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir o projeto " + projeto.getId() + "?"));

                Button confirmButton = new Button("Confirmar", event -> {
                    deleteProjeto(projeto);
                    confirm.close();
                });
                Button cancel = new Button("Cancelar", event -> confirm.close());

                confirm.getFooter().add(confirmButton, cancel);
                confirm.add(content);
                confirm.open();
            });
            delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
            return delete;
        }).setHeader("Ações");

        grid.addItemClickListener(event -> {
            Projeto projeto = event.getItem();
            grid.setDetailsVisible(projeto, !grid.isDetailsVisible(projeto));
        });

        grid.addItemDoubleClickListener(event -> {
            Projeto projeto = event.getItem();
            editProjeto(projeto);
            tabSheet.setSelectedIndex(1);
        });

        List<Projeto> listaDeProjetos = projetoController.pesquisarTodos();
        if (listaDeProjetos == null) {
            listaDeProjetos = Collections.emptyList();
        }
        grid.setItems(listaDeProjetos);

        layout.add(layoutRow, space, grid);
        projetosContentDiv.add(layout);
        return projetosContentDiv;
    }

    private Div createAddProjetosContent(){
        Div addProjetosContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");
        Div space1 = new Div();
        space1.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        cliente = new ComboBox("Cliente");
        status = new ComboBox("Status");
        medidas = new TextField("Medidas");
        valor = new TextField("Valor");
        desconto = new TextField("Desconto");
        desconto.setPlaceholder("%");
        descricao = new TextArea("Descrição");
        valorfinal = new TextField("Valor Final");
        data = new DatePicker("Data");
        cliente.addClassName("rounded-text-field");
        status.addClassName("rounded-text-field");
        medidas.addClassName("rounded-text-field");
        valor.addClassName("rounded-text-field");
        desconto.addClassName("rounded-text-field");
        descricao.addClassName("rounded-text-field");
        valorfinal.addClassName("rounded-text-field");
        data.addClassName("rounded-text-field");
        cliente.setRequiredIndicatorVisible(true);
        status.setRequiredIndicatorVisible(true);
        descricao.setRequiredIndicatorVisible(true);
        descricao.setMaxLength(255);
        medidas.setMaxLength(80);
        valor.setMaxLength(20);
        desconto.setMaxLength(20);
        data.setRequiredIndicatorVisible(true);
        valorfinal.setReadOnly(true);
        setComboBoxClienteData(cliente);
        layout3.setAlignItems(FlexComponent.Alignment.END);
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");
        status.setItems(StatusProjeto.values());
        valor.addValueChangeListener(event -> atualizarValorFinal());
        desconto.addValueChangeListener(event -> atualizarValorFinal());

        Button saveButton = new Button("Salvar", event -> {
            if (cliente.isEmpty() || status.isEmpty() || data.isEmpty() || descricao.isEmpty()) {
                Notification.show("Preencha os campos obrigatórios: Cliente, Status, Data e Descrição", 3000, Notification.Position.MIDDLE);
                return;
            }

            Cliente clienteProjeto = cliente.getValue();
            LocalDate dataProjeto = data.getValue();
            StatusProjeto statusProjeto = status.getValue();
            String descricaoProjeto = descricao.getValue();
            String medidasProjeto = medidas.isEmpty() ? "" : medidas.getValue();
            double valorProjeto = valor.isEmpty() ? 0.0 : Double.parseDouble(valor.getValue());
            double descontoProjeto = desconto.isEmpty() ? 0.0 : Double.parseDouble(desconto.getValue());
            double valorFinalProjeto = valorProjeto - descontoProjeto;

            Projeto projeto = new Projeto(statusProjeto, dataProjeto, valorProjeto, descontoProjeto, valorFinalProjeto, descricaoProjeto, medidasProjeto, clienteProjeto);

            boolean sucesso = false;
            if (projetoId != null && projetoId > 0) {
                projeto.setId(projetoId);
                sucesso = projetoController.alterar(projeto);
                if (sucesso) {
                    Notification.show("Projeto atualizado com sucesso!");
                    clearForm();
                } else {
                    Notification.show("Erro ao atualizar o projeto", 3000, Notification.Position.MIDDLE);
                }
            } else {
                sucesso = projetoController.inserir(projeto);
                if (sucesso) {
                    Notification.show("Projeto salvo com sucesso!");
                    clearForm();
                } else {
                    Notification.show("Erro ao salvar o projeto", 3000, Notification.Position.MIDDLE);
                }
            }
            if (sucesso) {
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        formLayout2Col.add(cliente, data, status, descricao, medidas, valor, desconto, valorfinal);
        layout2.add(formLayout2Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addProjetosContentDiv.add(space1, layout);
        return addProjetosContentDiv;
    }
    
    private void setComboBoxClienteData(ComboBox<Cliente> comboBox) {
        List<Cliente> clientes = clienteController.pesquisarTodos();
        comboBox.setItems(clientes);
        comboBox.setItemLabelGenerator(cliente -> cliente.getNome());
    }

    private void refreshGrid(){
        List<Projeto> projetos = projetoController.pesquisarTodos();
        grid.setItems(projetos);
    }

    private void deleteProjeto(Projeto projeto) {
        boolean success = projetoController.excluir(projeto);
        if (success) {
            refreshGrid();
        } else {
            Notification.show("Erro ao excluir projeto", 3000, Notification.Position.MIDDLE);
        }
    }

    private void editProjeto(Projeto projeto) {
        projetoId = projeto.getId();
        cliente.setValue(projeto.getCliente());
        valor.setValue(String.valueOf(projeto.getValor()));
        desconto.setValue(String.valueOf(projeto.getDesconto()));
        valorfinal.setValue(String.valueOf(projeto.getValorFinal()));
        data.setValue(projeto.getData());
        descricao.setValue(projeto.getDescricao());
        status.setValue(projeto.getStatusPr());
        medidas.setValue(projeto.getMedidas());
    }

    private void clearForm() {
        projetoId = null;
        cliente.clear();
        status.clear();
        valor.clear();
        desconto.clear();
        valorfinal.clear();
        descricao.clear();
        data.clear();
        medidas.clear();
    }

    private void atualizarValorFinal() {
        double valorProjeto = 0.0;
        double descontoPercentual = 0.0;
        
        try {
            if (!valor.isEmpty()) {
                valorProjeto = Double.parseDouble(valor.getValue());
            }
            if (!desconto.isEmpty()) {
                descontoPercentual = Double.parseDouble(desconto.getValue());
            }
        } catch (NumberFormatException e) {
            Notification.show("Insira valores numéricos válidos para Valor e Desconto", 3000, Notification.Position.MIDDLE);
            return;
        }
    
        double valorFinalProjeto = valorProjeto - (valorProjeto * (descontoPercentual / 100.0));
        valorFinalProjeto = Math.max(0, valorFinalProjeto);
        valorfinal.setValue(String.format("%.2f", valorFinalProjeto));
    }
    
    private static ComponentRenderer<HorizontalLayout, Projeto> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(projeto -> {
            HorizontalLayout detailsLayout = new HorizontalLayout();
            detailsLayout.setSpacing(true);
            detailsLayout.setPadding(true);
            detailsLayout.addClassName("details-layout");
    
            TextArea descricaoField = new TextArea("Descrição");
            descricaoField.setValue(projeto.getDescricao());
            descricaoField.setReadOnly(true);
            descricaoField.addClassName("rounded-text-field");

            TextField medidasField = new TextField("Medidas");
            medidasField.setValue(projeto.getMedidas());
            medidasField.setReadOnly(true);
            medidasField.addClassName("rounded-text-field");

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            String valorFormatado = currencyFormat.format(projeto.getValor());
            String valorfinalFormatado = currencyFormat.format(projeto.getValorFinal());

            TextField valorField = new TextField("Valor");
            valorField.setValue(valorFormatado);
            valorField.setReadOnly(true);
            valorField.addClassName("rounded-text-field");

            TextField valorfinalField = new TextField("Valor Final");
            valorfinalField.setValue(valorfinalFormatado);
            valorfinalField.setReadOnly(true);
            valorfinalField.addClassName("rounded-text-field");
    
            TextField descontoField = new TextField("Desconto");
            descontoField.setValue(String.valueOf(projeto.getDesconto()));
            descontoField.setReadOnly(true);
            descontoField.addClassName("rounded-text-field");

            detailsLayout.add(descricaoField, medidasField, valorField, descontoField, valorfinalField);
            return detailsLayout;
        });
    }
}