package com.example.application.views.fornecedor;

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
import com.example.application.model.Fornecedor;
import com.example.application.views.MainLayout;

@PageTitle("Fornecedor")
@Route(value = "my-view3", layout = MainLayout.class)
public class FornecedorView extends Composite<VerticalLayout> {

    public FornecedorView() {
        TabSheet tabSheet = new TabSheet();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        getContent().add(tabSheet);
    }

    //This method sets up two tabs
    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div fornecedoresContent = createFornecedoresContent();
        tabSheet.add("Fornecedores", fornecedoresContent);

        Div addFornecedorContent = createAddFornecedorContent();
        tabSheet.add("Adicionar Fornecedor", addFornecedorContent);
    }

    //This method creates the content for the "Fornecedores" tab, which consists of a form to see all the suppliers and search for a specific suppliers
    private Div createFornecedoresContent(){
        Div fornecedorContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();
        Grid<Fornecedor> minimalistGrid = new Grid(Fornecedor.class, false);

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

        minimalistGrid.addColumn(Fornecedor::getNome).setHeader("Nome");
        minimalistGrid.addColumn(Fornecedor::getDescriProdu).setHeader("Produto");
        minimalistGrid.addColumn(Fornecedor::getCpf).setHeader("CNPJ/CPF");
        minimalistGrid.addColumn(Fornecedor::getRg).setHeader("IE/RG");
        minimalistGrid.addColumn(Fornecedor::getTelefone).setHeader("Telefone");

        layout.add(layoutRow, space, minimalistGrid);
        fornecedorContentDiv.add(layout);

        return fornecedorContentDiv;
    }

    //This method creates the content for the "Adicionar Fornecedor" tab, which consists of a form to add a new supplier.
    private Div createAddFornecedorContent(){
        Div addFornecedorContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        TextField nome = new TextField("Nome");
        TextArea produto = new TextArea("Descrição do Produto Fornecido");
        TextField cpf = new TextField("CNPJ/CPF");
        TextField rg = new TextField("IE/RG");
        TextField telefone = new TextField("Telefone");

        Button saveButton = new Button("Salvar", event -> {
            Notification.show("Client saved!");
        });

        //For a better interface
        nome.addClassName("rounded-text-field");
        cpf.addClassName("rounded-text-field");
        rg.addClassName("rounded-text-field");
        telefone.addClassName("rounded-text-field");
        produto.addClassName("rounded-text-field");
        nome.setRequiredIndicatorVisible(true);
        telefone.setRequiredIndicatorVisible(true);
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout3.setAlignItems(FlexComponent.Alignment.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        
        formLayout2Col.add(nome, cpf, rg, produto, telefone);
        layout2.add(formLayout2Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addFornecedorContentDiv.add(layout);

        return addFornecedorContentDiv;
    }
}
