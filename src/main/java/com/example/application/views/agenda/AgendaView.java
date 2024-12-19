package com.example.application.views.agenda;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import com.example.application.controller.AgendaController;
import com.example.application.controller.FuncionarioController;
import com.example.application.model.Agenda;
import com.example.application.model.Cliente;
import com.example.application.model.Funcionario;
import com.example.application.model.StatusAgenda;
import com.example.application.views.MainLayout;

@PageTitle("Agenda")
@Route(value = "my-view6", layout = MainLayout.class)
public class AgendaView extends VerticalLayout {

    private FuncionarioController funcionarioController = new FuncionarioController();
    private AgendaController agendaController = new AgendaController();
    private Grid<Agenda> grid = new Grid<>(Agenda.class, false);
    private TextField titulo = new TextField("Título");
    private TextArea descricao = new TextArea("Descrição");
    private TextArea endereco = new TextArea("Endereço");
    private DateTimePicker data = new DateTimePicker();
    private ComboBox<Funcionario> funcionario = new ComboBox<>("Funcionario");
    private ComboBox<StatusAgenda> status = new ComboBox<>("Status");
    private Long agendaId;
    private TabSheet tabSheet;

    public AgendaView() {
        tabSheet = new TabSheet();
        this.setWidth("100%");
        this.getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        this.add(tabSheet);
    }

    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div agendaContent = createAgendaContent();
        tabSheet.add("Agenda", agendaContent);
        Div addAgendaContent = createAddAgendaContent();
        tabSheet.add("Adicionar agendamento", addAgendaContent);
    }

    private Div createAgendaContent() {
        Div osContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();
        textField.setPlaceholder("Título, Data, Status ou Funcionario");
        textField.setWidth("290px");
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

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<Agenda> resultados;

            if (pesquisa.isEmpty()) {
                resultados = agendaController.pesquisarTodos();
            } else {
                resultados = agendaController.pesquisarAgenda(pesquisa);
            }
            if (resultados.isEmpty()) {
                Notification.show("Nenhum resultado encontrado para: " + pesquisa);
            }

            grid.setItems(resultados);
        });

        grid = createGrid();

        layout.add(layoutRow, space, grid);
        osContentDiv.add(layout);

        return osContentDiv;
    }

    private Div createAddAgendaContent() {
        Div addOSContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");
        Div space1 = new Div();
        space1.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        status = new ComboBox<>("Status");
        titulo = new TextField("Título");
        titulo.setMaxLength(50);
        descricao = new TextArea("Descrição");
        descricao.setMaxLength(255);
        data = new DateTimePicker("Data");
        data.setStep(Duration.ofMinutes(1));
        funcionario = new ComboBox<>("Funcionario");
        endereco = new TextArea("Endereço");
        endereco.setMaxLength(255);
        status.setItems(StatusAgenda.values());
        setComboBoxFuncionarioData(funcionario);
        status.addClassName("rounded-text-field");
        titulo.addClassName("rounded-text-field");
        descricao.addClassName("rounded-text-field");
        endereco.addClassName("rounded-text-field");
        funcionario.addClassName("rounded-text-field");
        titulo.setRequiredIndicatorVisible(true);
        layout3.setAlignItems(FlexComponent.Alignment.END);
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");

        Button saveButton = new Button("Salvar", event -> {
            if (titulo.isEmpty()) {
                Notification.show("Preencha o campo obrigatório: título", 3000, Notification.Position.MIDDLE);
                return;
            }
            StatusAgenda statusAgenda = status.getValue();
            String tituloAgenda = titulo.getValue();
            String descricaoAgenda = descricao.isEmpty() ? "" : descricao.getValue();
            String enderecoAgenda = endereco.isEmpty() ? "" : endereco.getValue();
            LocalDateTime dataFuncionario = data.isEmpty() ? null  : data.getValue();
            Funcionario funcionarioAgenda = funcionario.isEmpty() ? null : funcionario.getValue();

            Agenda agenda = new Agenda(statusAgenda, tituloAgenda,
            descricaoAgenda, enderecoAgenda, dataFuncionario, funcionarioAgenda);
            agenda.setId(agendaId);

            boolean sucesso;
            if (agendaId != null && agendaId > 0) {
                sucesso = agendaController.alterar(agenda);
                if (sucesso) {
                    Notification.show("Agendamento atualizado com sucesso!");
                } else {
                    Notification.show("Erro ao atualizar a agendamento", 3000, Notification.Position.MIDDLE);
                }
            } else {
                sucesso = agendaController.inserir(agenda);
                if (sucesso) {
                    Notification.show("Agendamento salvo com sucesso!");
                } else {
                    Notification.show("Erro ao salvar agendamento", 3000, Notification.Position.MIDDLE);
                }
            }
            if (sucesso) {
                clearForm();
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        formLayout2Col.add(titulo, data, status, funcionario, descricao, endereco);
        layout2.add(formLayout2Col, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addOSContentDiv.add(space1, layout);

        return addOSContentDiv;
    }

    private void setComboBoxFuncionarioData(ComboBox<Funcionario> comboBox) {
        List<Funcionario> funcionarios = funcionarioController.pesquisarTodos();
        comboBox.setItems(funcionarios);
        comboBox.setItemLabelGenerator(funcionario -> funcionario.getNome());
    }

    private Grid<Agenda> createGrid() {
        grid = new Grid<>(Agenda.class, false);
        grid.addClassName("borderless-grid");
        grid.setAllRowsVisible(true);
        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createAgendaDetailsRenderer());

        grid.addColumn(Agenda::getTitulo).setHeader("Titulo").setSortable(true);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        grid.addColumn(agenda -> agenda.getDataHora() != null ? agenda.getDataHora().format(formatter) : "")
            .setHeader("Data")
            .setSortable(true);

        grid.addColumn(new ComponentRenderer<>(agenda -> {
            Span statusBadge = new Span(agenda.getStatus() != null ? agenda.getStatus().getDescricao() : "Indefinido");
            statusBadge.addClassName("status-badge");
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

        grid.addComponentColumn(agenda -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir esse agendamento " + agenda.getTitulo() + "?"));

                Button confirmButton = new Button("Confirmar", event -> {
                    deleteAgenda(agenda);
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
            Agenda agenda = event.getItem();
            grid.setDetailsVisible(agenda, !grid.isDetailsVisible(agenda));
        });

        grid.addItemDoubleClickListener(event -> {
            Agenda agenda = event.getItem();
            editAgenda(agenda);
            tabSheet.setSelectedIndex(1);
        });

        List<Agenda> listaDeAgendas = agendaController.pesquisarTodos();
        if (listaDeAgendas == null) {
            listaDeAgendas = Collections.emptyList();
        }
        grid.setItems(listaDeAgendas);

        return grid;
    }

    private void refreshGrid(){
        List<Agenda> agendas = agendaController.pesquisarTodos();
        grid.setItems(agendas);
    }

    private void deleteAgenda(Agenda agenda){
        boolean sucess = agendaController.excluir(agenda);
        if(sucess){
            refreshGrid();
        }else{
            Notification.show("Erro ao excluir agendamento", 3000, Notification.Position.MIDDLE);
        }
    }

    private void editAgenda(Agenda agenda) {
        agendaId = agenda.getId();
        status.setValue(agenda.getStatus());
        titulo.setValue(agenda.getTitulo());
        descricao.setValue(agenda.getDescricao());
        endereco.setValue(agenda.getEndereco());
        data.setValue(agenda.getDataHora());
        funcionario.setValue(agenda.getFuncionario());
    }

    private void clearForm(){
        agendaId = null;
        status.clear();
        titulo.clear();
        descricao.clear();
        endereco.clear();
        data.clear();
        funcionario.clear();
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
}