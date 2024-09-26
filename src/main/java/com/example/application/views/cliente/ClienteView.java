package com.example.application.views.cliente;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.button.ButtonVariant;

@PageTitle("Cliente")
@Menu(icon = "line-awesome/svg/pencil-ruler-solid.svg", order = 1)
@Route(value = "my-view")
public class ClienteView extends Composite<VerticalLayout> {

    public ClienteView() {
        TabSheet tabSheet = new TabSheet();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        getContent().add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div clientesContent = createClientesContent();
        tabSheet.add("Clientes", clientesContent);

        Div addClientesContent = createAddClientesContent();
        tabSheet.add("Adicionar Cliente", addClientesContent);
    }

    private Div createClientesContent(){
        Div clientesContentDiv = new Div();
        VerticalLayout layout = new VerticalLayout();

        return clientesContentDiv;
    }

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
        
        Button saveButton = new Button("Salvar", event -> {
            Notification.show("Client saved!");
        });
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
