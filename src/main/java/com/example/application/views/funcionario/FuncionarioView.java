package com.example.application.views.funcionario;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.example.application.model.Funcionario;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.example.application.views.MainLayout;


@PageTitle("Funcionário")
@Route(value = "my-view2", layout = MainLayout.class)
public class FuncionarioView extends Composite<VerticalLayout> {

    public FuncionarioView() {
        TabSheet tabSheet = new TabSheet();
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

    //This method creates the content for the "Funcionários" tab, which consists of a form to see all the employees and search for a specific employee
    private Div createFuncionariosContent(){
        Div funcionarioContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();
        Grid<Funcionario> minimalistGrid = new Grid(Funcionario.class, false);

        //For a better interface
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
        minimalistGrid.setAllRowsVisible(true);

        buttonPrimary.addClickListener(event -> {
            Notification.show("Search for: " + textField.getValue());
        });

        minimalistGrid.addColumn(Funcionario::getNome).setHeader("Nome");
        minimalistGrid.addColumn(Funcionario::getCpf).setHeader("CNPJ/CPF");
        minimalistGrid.addColumn(Funcionario::getRg).setHeader("IE/RG");
        minimalistGrid.addColumn(Funcionario::getTelefone).setHeader("Telefone");
        minimalistGrid.addColumn(Funcionario::getEndereco).setHeader("Endereço");
        minimalistGrid.addColumn(Funcionario::getDataAdmissao).setHeader("Admissão");
        minimalistGrid.addColumn(Funcionario::getSalario).setHeader("Salário");

        layout.add(layoutRow, space, minimalistGrid);
        funcionarioContentDiv.add(layout);

        return funcionarioContentDiv;
    }

   //This method creates the content for the "Adicionar Funcionário" tab, which consists of a form to add a new employee.
    private Div createAddFuncionariosContent(){
        Div addFuncionariosContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        TextField nome = new TextField("Nome");
        TextField cpf = new TextField("CNPJ/CPF");
        TextField rg = new TextField("IE/RG");
        TextField telefone = new TextField("Telefone");
        TextArea endereco = new TextArea("Endereço");
        TextField salario = new TextField("Salário");
        DatePicker admissao = new DatePicker("Data Admissão");

        Button saveButton = new Button("Salvar", event -> {
            Notification.show("Client saved!");
        });

        //For a better interface
        nome.addClassName("rounded-text-field");
        cpf.addClassName("rounded-text-field");
        rg.addClassName("rounded-text-field");
        telefone.addClassName("rounded-text-field");
        endereco.addClassName("rounded-text-field");
        salario.addClassName("rounded-text-field");
        admissao.addClassName("rounded-text-field");
        nome.setRequiredIndicatorVisible(true);
        telefone.setRequiredIndicatorVisible(true);
        endereco.setWidth("100%");
        endereco.setHeight("90px");
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout3.setAlignItems(FlexComponent.Alignment.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        
        formLayout2Col.add(nome, cpf, rg, telefone, admissao, salario);
        layout2.add(formLayout2Col, endereco, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addFuncionariosContentDiv.add(layout);

        return addFuncionariosContentDiv;
    }

}
