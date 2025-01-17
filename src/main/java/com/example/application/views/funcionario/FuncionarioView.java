package com.example.application.views.funcionario;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
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

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.example.application.model.Funcionario;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.example.application.views.MainLayout;
import com.example.application.controller.FuncionarioController;


@PageTitle("Funcionário")
@Route(value = "my-view2", layout = MainLayout.class)
public class FuncionarioView extends Composite<VerticalLayout> {

    private FuncionarioController funcionarioController = new FuncionarioController();
    private Grid<Funcionario> grid = new Grid<>();
    private TextField nome = new TextField();
    private TextField cpf = new TextField();
    private TextField rg = new TextField();
    private TextField telefone = new TextField();
    private DatePicker data = new DatePicker();
    private TextField salario = new TextField();
    private Long funcionarioId;
    private TabSheet tabSheet;

    public FuncionarioView() {
        tabSheet = new TabSheet();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        getContent().add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div funcionarioContent = createFuncionariosContent();
        tabSheet.add("Funcionários", funcionarioContent);
        Div addFuncionariosContent = createAddFuncionariosContent();
        tabSheet.add("Adicionar Funcionário", addFuncionariosContent);
    }

    private Div createFuncionariosContent(){
        Div funcionarioContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();
        textField.setPlaceholder("Nome, CNPJ/CPF ou IE/RG");
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
        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createPersonDetailsRenderer());

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<Funcionario> resultados;
            if(pesquisa.isEmpty()){
                resultados = funcionarioController.pesquisarTodos();
            }else {
                resultados = funcionarioController.pesquisarFuncionario(pesquisa);
            }
            grid.setItems(resultados);
        });

        grid.addColumn(Funcionario::getNome).setHeader("Nome");
        grid.addColumn(Funcionario::getTelefone).setHeader("Telefone");
        grid.addColumn(Funcionario::getDataAdmissao).setHeader("Admissão");

        grid.addComponentColumn(funcionario -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir o funcionario"));

                Button confirmButton = new Button("Confirmar", event -> {
                    deleteFuncionario(funcionario);
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
            Funcionario funcionario = event.getItem();
            grid.setDetailsVisible(funcionario, !grid.isDetailsVisible(funcionario));
        });

        grid.addItemDoubleClickListener(event -> {
            Funcionario funcionario = event.getItem();
            editFuncionario(funcionario);
            tabSheet.setSelectedIndex(1);
        });

        List<Funcionario> listaDeFuncionarios = funcionarioController.pesquisarTodos();
        if (listaDeFuncionarios == null) {
            listaDeFuncionarios = Collections.emptyList();
        }
        grid.setItems(listaDeFuncionarios);

        layout.add(layoutRow, space, grid);
        funcionarioContentDiv.add(layout);

        return funcionarioContentDiv;
    }

    private Div createAddFuncionariosContent(){
        Div addFuncionariosContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");
        Div space1 = new Div();
        space1.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        nome = new TextField("Nome");
        nome.setMaxLength(100);
        cpf = new TextField("CPF");
        rg = new TextField("RG");
        telefone = new TextField("Telefone");
        salario = new TextField("Salário");
        data = new DatePicker("Data Admissão");
        salario.setPlaceholder("Exemplo: 2000.00 ou 2000");
        nome.addClassName("rounded-text-field");
        cpf.addClassName("rounded-text-field");
        rg.addClassName("rounded-text-field");
        telefone.addClassName("rounded-text-field");
        salario.addClassName("rounded-text-field");
        data.addClassName("rounded-text-field");
        nome.setRequiredIndicatorVisible(true);
        telefone.setRequiredIndicatorVisible(true);
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");
        layout3.setAlignItems(FlexComponent.Alignment.END);

        cpf.addBlurListener(event -> {
            String value = cpf.getValue().replaceAll("[^\\d]", "");
            if (value.length() == 11) {
                value = value.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
            }
            cpf.setValue(value);
        });
        cpf.setMaxLength(14);

        rg.addBlurListener(event -> {
            String value = rg.getValue().replaceAll("[^\\d]", "");
            if (value.length() == 9) {
                value = value.replaceAll("(\\d{2})(\\d{3})(\\d{3})(\\d)", "$1.$2.$3-$4");
            }
            rg.setValue(value);
        });
        rg.setMaxLength(12);

        telefone.addBlurListener(event -> {
            String value = telefone.getValue().replaceAll("[^\\d]", "");
            if (value.length() == 10) {
                value = value.replaceAll("(\\d{2})(\\d{4})(\\d{4})", "($1) $2-$3");
            } else if (value.length() == 11) {
                value = value.replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3");
            }

            telefone.setValue(value);
        });
        telefone.setMaxLength(15);

        salario.addValueChangeListener(event -> {
            String valor = event.getValue();
            if (valor != null) {
                if (valor.contains(",")) {
                    Notification.show("Por favor, use '.' em vez de ',' para separar decimais.", 3000, Notification.Position.MIDDLE);
                    salario.clear();
                }
                else if (!valor.matches("\\d*\\.?\\d*")) {
                    Notification.show("O valor deve conter apenas números e, opcionalmente, um ponto para separar decimais.", 3000, Notification.Position.MIDDLE);
                    salario.clear();
                }
            }
        });

        Button saveButton = new Button("Salvar", event -> {
            if(nome.isEmpty() || telefone.isEmpty()){
                Notification.show("Preencha os campos obrigatórios: Nome e Telefone", 3000, Notification.Position.MIDDLE);
                return;
            }

            String nomeFuncionario = nome.getValue();
            String cpfFucionario = cpf.isEmpty() ? "" : cpf.getValue();
            String rgFuncionario = rg.isEmpty() ? "" : rg.getValue();
            String telefoneFuncionario = telefone.getValue();
            LocalDate dataFuncionario = data.isEmpty() ? null : data.getValue();
            Double salarioValue = salario.isEmpty() ? 0.0 : Double.parseDouble(salario.getValue());

            Funcionario funcionario = new Funcionario(nomeFuncionario, cpfFucionario, rgFuncionario, telefoneFuncionario, dataFuncionario, salarioValue);
            funcionario.setId(funcionarioId);

            boolean sucesso;
            if(funcionarioId != null && funcionarioId > 0) {
                sucesso = funcionarioController.alterar(funcionario);
                if(sucesso) {
                    Notification.show("Funcionario atualizado com sucesso!");
                    clearForm();
                }else{
                    Notification.show("Erro ao atualizar o funcionario");
                }
            } else {
                sucesso = funcionarioController.inserir(funcionario);
                if(sucesso) {
                    Notification.show("Funcionario salvo com sucesso!");
                    clearForm();
                } else {
                    Notification.show("Erro ao salvar funcionario", 3000, Notification.Position.MIDDLE);
                }
            }
            if(sucesso) {
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        formLayout2Col.add(nome, cpf, rg, telefone, data, salario);
        layout2.add(formLayout2Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addFuncionariosContentDiv.add(space1, layout);
        return addFuncionariosContentDiv;
    }

    private void refreshGrid(){
        List<Funcionario> funcionarios = funcionarioController.pesquisarTodos();
        grid.setItems(funcionarios);
    }

    private void deleteFuncionario(Funcionario funcionario) {
        String errorMessage = funcionarioController.excluir(funcionario);
        if (errorMessage == null) {
            Notification notification = new Notification(
                    "Funcionário excluído com sucesso.", 3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
            refreshGrid();
        } else {
            Notification notification = new Notification(
                    errorMessage, 3000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.open();
        }
    }

    private void editFuncionario(Funcionario funcionario){
        funcionarioId = funcionario.getId();
        nome.setValue(funcionario.getNome());
        cpf.setValue(String.valueOf(funcionario.getCpf()));
        rg.setValue(String.valueOf(funcionario.getRg()));
        telefone.setValue(String.valueOf(funcionario.getTelefone()));
        salario.setValue(String.valueOf(funcionario.getSalario()));
        data.setValue(funcionario.getDataAdmissao());
    }

    private void clearForm(){
        funcionarioId = null;
        nome.clear();
        cpf.clear();
        rg.clear();
        telefone.clear();
        salario.clear();
        data.clear();
    }

    private static ComponentRenderer<HorizontalLayout, Funcionario> createPersonDetailsRenderer() {
        return new ComponentRenderer<>(funcionario -> {
            HorizontalLayout detailsLayout = new HorizontalLayout();
            detailsLayout.setSpacing(true);
            detailsLayout.setPadding(true);
            detailsLayout.addClassName("details-layout");
    
            TextField cpfField = new TextField("CPF");
            cpfField.setValue(funcionario.getCpf());
            cpfField.setReadOnly(true);
            cpfField.addClassName("rounded-text-field");
    
            TextField rgField = new TextField("RG");
            rgField.setValue(funcionario.getRg());
            rgField.setReadOnly(true);
            rgField.addClassName("rounded-text-field");

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
            String salarioFormatado = currencyFormat.format(funcionario.getSalario());
            TextField salarioField = new TextField("Salário");
            salarioField.setValue(salarioFormatado);
            salarioField.setReadOnly(true);
            salarioField.addClassName("rounded-text-field");
    
            detailsLayout.add(cpfField, rgField, salarioField);
            return detailsLayout;
        });
    }
}