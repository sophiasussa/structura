package com.example.application.views.cliente;

import com.example.application.model.Cliente;
import com.example.application.repository.DaoCliente;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.example.application.views.MainLayout;


import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.button.ButtonVariant;

@PageTitle("Cliente")
@Route(value = "my-view", layout = MainLayout.class)
public class ClienteView extends Composite<VerticalLayout> {

    DaoCliente clienteRepository = new DaoCliente();

    public ClienteView() {
        TabSheet tabSheet = new TabSheet();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        getContent().add(tabSheet);
    }

    //This method sets up two tabs
    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div clientesContent = createClientesContent();
        tabSheet.add("Clientes", clientesContent);

        Div addClientesContent = createAddClientesContent();
        tabSheet.add("Adicionar Cliente", addClientesContent);
    }

    //This method creates the content for the "Clientes" tab, which consists of a form to see all the clients and search for a specific client
    private Div createClientesContent(){
        Div clientesContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();
        Grid<Cliente> minimalistGrid = new Grid(Cliente.class, false);

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

        minimalistGrid.addColumn(Cliente::getNome).setHeader("Nome");
        minimalistGrid.addColumn(Cliente::getCpf).setHeader("CNPJ/CPF");
        minimalistGrid.addColumn(Cliente::getRg).setHeader("IE/RG");
        minimalistGrid.addColumn(Cliente::getTelefone).setHeader("Telefone");
        minimalistGrid.addColumn(Cliente::getEndereco).setHeader("Endereço");

        layout.add(layoutRow, space, minimalistGrid);
        clientesContentDiv.add(layout);

        return clientesContentDiv;
    }

    //This method creates the content for the "Adicionar Cliente" tab, which consists of a form to add a new client.
    private Div createAddClientesContent(){
        Div addClientesContentDiv = new Div();
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

        Button saveButton = new Button("Salvar", event -> {
            if(nome.isEmpty() || telefone.isEmpty()){
                Notification.show("Preencha os campos obrigatórios: Nome e Telefone", 3000, Notification.Position.MIDDLE);
                return;
            }

            String nomeCliente = nome.getValue();
            long cpfCliente = cpf.isEmpty() ? 0 : Long.parseLong(cpf.getValue());
            long rgCliente = rg.isEmpty() ? 0 : Long.parseLong(rg.getValue());
            long telefoneCliente = Long.parseLong(telefone.getValue());
            String enderecoCliente = endereco.getValue();

            Cliente cliente = new Cliente(nomeCliente, cpfCliente, rgCliente, telefoneCliente, enderecoCliente);
        
            boolean sucesso = clienteRepository.inserir(cliente);

            if(sucesso){
                Notification.show("Cliente salvo com sucesso!");
            }else{
                Notification.show("Erro ao salvar o cliente", 3000, Notification.Position.MIDDLE);
            }
        });

        //For a better interface
        nome.addClassName("rounded-text-field");
        cpf.addClassName("rounded-text-field");
        rg.addClassName("rounded-text-field");
        telefone.addClassName("rounded-text-field");
        endereco.addClassName("rounded-text-field");
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
        
        formLayout2Col.add(nome, cpf, rg, telefone);
        layout2.add(formLayout2Col, endereco, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addClientesContentDiv.add(layout);

        return addClientesContentDiv;
    }
}
