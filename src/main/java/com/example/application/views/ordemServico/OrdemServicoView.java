package com.example.application.views.ordemServico;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.application.model.Cliente;
import com.example.application.model.Funcionario;
import com.example.application.model.OrdemServico;
import com.example.application.model.Produto;
import com.example.application.model.ProdutoOS;
import com.example.application.model.StatusOS;
import com.example.application.repository.DaoCliente;
import com.example.application.repository.DaoFuncionario;
import com.example.application.repository.DaoOSProduto;
import com.example.application.repository.DaoOrdemServico;
import com.example.application.repository.DaoProduto;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

@PageTitle("Ordem de Servico")
@Route(value = "my-view5", layout = MainLayout.class)
public class OrdemServicoView extends VerticalLayout {

    DaoProduto produtoRepository = new DaoProduto();
    DaoFuncionario funcionarioRepository = new DaoFuncionario();
    DaoCliente clienteRepository = new DaoCliente();
    DaoOrdemServico osRepository;
    DaoOSProduto osProdutoRepository;
    Grid<OrdemServico> grid = new Grid(OrdemServico.class, false);
    TextField status = new TextField("Status");
    TextField endereco = new TextField("Endereço");
    TextField observacao = new TextField("Observação");
    DatePicker data = new DatePicker("Data");
    ComboBox<Cliente> cliente = new ComboBox<>("Cliente");
    ComboBox<Funcionario> funcionario = new ComboBox<>("Funcionario");
    MultiSelectComboBox<Produto> produto = new MultiSelectComboBox<>("Produto");
    TextField imagemPathField;
    Button buttonTertiary = new Button();
    Button buttonTertiary2 = new Button();
    Button buttonTertiary3 = new Button();
    private Long osId;
    private TabSheet tabSheet;
    private String image;

    public OrdemServicoView() {
        tabSheet = new TabSheet();
        this.setWidth("100%");
        this.getStyle().set("flex-grow", "1");
        tabSheet.setWidth("100%");
        setTabSheetSampleData(tabSheet);
        this.add(tabSheet);
    }

    // This method sets up two tabs
    private void setTabSheetSampleData(TabSheet tabSheet) {
        Div osContent = createOSContent();
        tabSheet.add("Ordens de Serviço", osContent);

        Div addOSContent = createAddOSContent();
        tabSheet.add("Adicionar Ordem de Serviço", addOSContent);
    }

    // This method creates the content for the "Ordens de Serviço" tab, which
    // consists of a
    // form to see all the Ordens de Serviço and search for a specific os
    private Div createOSContent() {
        Div osContentDiv = new Div();
        Div space = new Div();
        space.setHeight("15px");

        VerticalLayout layout = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        TextField textField = new TextField("Pesquisar");
        Button buttonPrimary = new Button();

        buttonPrimary.addClickListener(event -> {
            String pesquisa = textField.getValue().trim();
            List<OrdemServico> resultados;

            if (pesquisa.isEmpty()) {
                resultados = osRepository.getAllOrdensServico();
            } else {
                resultados = osRepository.searchOS(pesquisa);
            }

            if (resultados.isEmpty()) {
                Notification.show("Nenhum resultado encontrado para: " + pesquisa);
            }

            grid.setItems(resultados);
        });

        // For a better interface
        textField.setPlaceholder("Nome ou Material");
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

        grid = createGrid();

        layout.add(layoutRow, space, grid);
        osContentDiv.add(layout);

        return osContentDiv;
    }

    // This method creates the content for the "Adicionar Ordem de Serviço" tab,
    // which
    // consists of a form to add a new Ordem de Serviço.
    private Div createAddOSContent() {
        Div addOSContentDiv = new Div();
        Div space = new Div();
        space.setHeight("10px");
        Div space1 = new Div();
        space1.setHeight("10px");

        VerticalLayout layout = new VerticalLayout();
        VerticalLayout layout2 = new VerticalLayout();
        VerticalLayout layout3 = new VerticalLayout();
        FormLayout formLayout2Col = new FormLayout();
        FormLayout formLayout3Col = new FormLayout();
        status = new TextField("Status");
        endereco = new TextField("Endereço");
        observacao = new TextField("Observação");
        data = new DatePicker("Data");
        cliente = new ComboBox<>("Cliente");
        setComboBoxClienteData(cliente);
        funcionario = new ComboBox<>("Funcionario");
        setComboBoxFuncionarioData(funcionario);
        produto = new MultiSelectComboBox<>("Produto");
        setComboBoxProdutoData(produto);

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(1);
        upload.setMaxFileSize(5 * 1024 * 1024);

        upload.addSucceededListener(event -> {
            try {
                String fileName = event.getFileName();
                String uploadDir = "C:/Users/jorda/Downloads/imagensMecanica/";
                File targetFile = new File(uploadDir + fileName);

                try (InputStream inputStream = buffer.getInputStream()) {
                    Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }

                image = targetFile.getAbsolutePath();
                imagemPathField.setValue(image);
                Notification.show("Imagem carregada com sucesso!", 3000, Notification.Position.MIDDLE);
            } catch (IOException e) {
                e.printStackTrace();
                Notification.show("Falha ao fazer upload da imagem: " + e.getMessage(), 3000,
                        Notification.Position.MIDDLE);
            }
        });

        imagemPathField = new TextField("Caminho da Imagem");
        imagemPathField.setWidthFull();
        imagemPathField.setReadOnly(true);

        Button saveButton = new Button("Salvar", event -> {
            if (cliente.isEmpty()) {
                Notification.show("Preencha o campo obrigatório: Cliente", 3000, Notification.Position.MIDDLE);
                return;
            }
            StatusOS statusOrdemServico = StatusOS.valueOf(status.getValue());
            String enderecoOrdemServico = endereco.isEmpty() ? null : endereco.getValue();
            String observacaoOrdemServico = observacao.isEmpty() ? null : observacao.getValue();
            LocalDate dataFuncionario = data.isEmpty() ? null : data.getValue();
            Cliente clienteOrdemServico = cliente.isEmpty() ? null : cliente.getValue();
            Funcionario funcionarioOrdemServico = funcionario.isEmpty() ? null : funcionario.getValue();
            Set<Produto> produtosSelecionados = produto.getValue();

            OrdemServico os = new OrdemServico(statusOrdemServico, clienteOrdemServico, enderecoOrdemServico,
                    funcionarioOrdemServico, dataFuncionario, observacaoOrdemServico);
            os.setId(osId);

            if (image != null) {
                List<String> imagens = List.of(image);
                os.setImagens(imagens);
            }

            List<Produto> produtosSelecionadosList = new ArrayList<>(produtosSelecionados);

            boolean sucesso;
            if (osId != null && osId > 0) {
                sucesso = osRepository.updateOrdemServico(os, produtosSelecionadosList);
                if (sucesso) {
                    Notification.show("OS atualizada com sucesso!");
                } else {
                    Notification.show("Erro ao atualizar o OS", 3000, Notification.Position.MIDDLE);
                }
            } else {
                sucesso = osRepository.saveOrdemServico(os, produtosSelecionadosList);
                if (sucesso) {
                    Notification.show("OS salvo com sucesso!");
                } else {
                    Notification.show("Erro ao salvar o OS", 3000, Notification.Position.MIDDLE);
                }
            }

            if (sucesso) {
                clearForm();
                tabSheet.setSelectedIndex(0);
                refreshGrid();
            }
        });

        // For a better interface
        status.addClassName("rounded-text-field");
        endereco.addClassName("rounded-text-field");
        observacao.addClassName("rounded-text-field");
        data.addClassName("rounded-text-field");
        funcionario.addClassName("rounded-text-field");
        produto.addClassName("rounded-text-field");
        cliente.addClassName("rounded-text-field");
        imagemPathField.addClassName("rounded-text-field");
        cliente.setRequiredIndicatorVisible(true);
        layout3.setAlignItems(FlexComponent.Alignment.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");

        formLayout3Col.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3));

        formLayout2Col.add(status, imagemPathField);
        formLayout3Col.add(cliente, data, funcionario);
        layout2.add(formLayout2Col, formLayout3Col, endereco, observacao, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addOSContentDiv.add(space1, layout);

        return addOSContentDiv;
    }

    private void setComboBoxProdutoData(MultiSelectComboBox<Produto> comboBox) {
        List<Produto> produtos = produtoRepository.pesquisarTodos();
        comboBox.setItems(produtos);
        comboBox.setItemLabelGenerator(produto -> produto.getNome());
    }

    private void setComboBoxClienteData(ComboBox<Cliente> comboBox) {
        List<Cliente> clientes = clienteRepository.pesquisarTodos();
        comboBox.setItems(clientes);
        comboBox.setItemLabelGenerator(cliente -> cliente.getNome());
    }

    private void setComboBoxFuncionarioData(ComboBox<Funcionario> comboBox) {
        List<Funcionario> funcionarios = funcionarioRepository.pesquisarTodos();
        comboBox.setItems(funcionarios);
        comboBox.setItemLabelGenerator(funcionario -> funcionario.getNome());
    }

    private Grid<OrdemServico> createGrid() {
        grid = new Grid<>(OrdemServico.class, false);
        grid.addClassName("borderless-grid");
        grid.setAllRowsVisible(true);

        grid.addColumn(ordemServico -> ordemServico.getId()).setHeader("ID").setSortable(true);
        grid.addColumn(OrdemServico::getStatusOS).setHeader("Status").setSortable(true);
        grid.addColumn(ordemServico -> ordemServico.getCliente().getNome()).setHeader("Cliente").setSortable(true);

        grid.addColumn(
                ordemServico -> ordemServico.getFuncionario() != null && ordemServico.getFuncionario().getNome() != null
                        ? ordemServico.getFuncionario().getNome()
                        : "Sem Funcionario")
                .setHeader("Funcionario").setSortable(true);

        grid.addComponentColumn(ordemServico -> {
            Button delete = new Button(VaadinIcon.TRASH.create(), e -> {
                Dialog confirm = new Dialog();
                confirm.setHeaderTitle("Confirmar Exclusão");
                VerticalLayout content = new VerticalLayout();
                content.add(new Text("Você tem certeza que deseja excluir essa OS " + ordemServico.getId() + "?"));

                Button confirmButton = new Button("Confirmar", event -> {
                    deleteOrdemServico(ordemServico);
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

        grid.setDetailsVisibleOnClick(false);
        grid.setItemDetailsRenderer(createOrdemServicoDetailsRenderer());

        grid.addItemClickListener(event -> {
            OrdemServico os = event.getItem();
            grid.setDetailsVisible(os, !grid.isDetailsVisible(os));
        });

        grid.addItemDoubleClickListener(event -> {
            OrdemServico os = event.getItem();
            editOrdemServico(os);
            tabSheet.setSelectedIndex(1);
        });

        grid.setItems(osRepository.getAllOrdensServico());

        return grid;
    }

    private void refreshGrid() {
        List<OrdemServico> oss = osRepository.getAllOrdensServico();
        grid.setItems(oss);
    }

    private void deleteOrdemServico(OrdemServico os) {
        boolean success = osRepository.deleteOrdemServico(os.getId());
        if (success) {
            refreshGrid();
        } else {
            System.out.println("Erro ao excluir OS");
        }
    }

    private void editOrdemServico(OrdemServico os) {
        osId = os.getId();
        status.setValue(String.valueOf(os.getStatusOS()));
        endereco.setValue(String.valueOf(os.getEndereco()));
        observacao.setValue(String.valueOf(os.getObservacao()));
        funcionario.setValue(os.getFuncionario());
        cliente.setValue(os.getCliente());
        data.setValue(os.getData());
        List<ProdutoOS> produtosSelecionados = osProdutoRepository.getProdutoOSsByOrdemServicoId(osId);
        Set<Produto> produtos = produtosSelecionados.stream()
            .map(ProdutoOS::getProduto)
            .collect(Collectors.toSet());
        produto.setValue(produtos);

        List<String> imagens = os.getImagens();

        if (imagens != null && !imagens.isEmpty()) {
            for (String imagePath : imagens) {
                System.out.println(imagePath);
            }
            imagemPathField.setValue(imagens.get(0));
            Notification.show("Imagem atual: " + imagens.get(0), 3000, Notification.Position.MIDDLE);
        } else {
            imagemPathField.clear();
            Notification.show("Nenhuma imagem associada.", 3000, Notification.Position.MIDDLE);
        }
    }

    private void clearForm() {
        osId = null;
        status.clear();
        endereco.clear();
        observacao.clear();
        cliente.clear();
        funcionario.clear();
        data.clear();
        produto.clear();
        image = null;
    }

    private static ComponentRenderer<HorizontalLayout, OrdemServico> createOrdemServicoDetailsRenderer() {
        return new ComponentRenderer<>(os -> {
            HorizontalLayout detailsLayout = new HorizontalLayout();
            detailsLayout.setSpacing(true);
            detailsLayout.setPadding(true);
            detailsLayout.addClassName("details-layout");

            TextField enderecoField = new TextField("Endereco");
            enderecoField.setValue(String.valueOf(os.getEndereco()));
            enderecoField.setReadOnly(true);
            enderecoField.addClassName("rounded-text-field");

            TextField observacaoField = new TextField("Observacao");
            observacaoField.setValue(String.valueOf(os.getObservacao()));
            observacaoField.setReadOnly(true);
            observacaoField.addClassName("rounded-text-field");

            DatePicker dataPicker = new DatePicker("Data");
            dataPicker.setValue(os.getData());
            dataPicker.setReadOnly(true);
            dataPicker.addClassName("rounded-text-field");

            MultiSelectComboBox prodMultiSelectComboBox = new MultiSelectComboBox("Custo Unitário");
            prodMultiSelectComboBox.setValue();
            prodMultiSelectComboBox.setReadOnly(true);
            prodMultiSelectComboBox.addClassName("rounded-text-field");

            detailsLayout.add(observacaoField, enderecoField, dataPicker, prodMultiSelectComboBox);

            return detailsLayout;
        });
    }
}
