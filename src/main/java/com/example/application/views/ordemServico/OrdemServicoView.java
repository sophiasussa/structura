package com.example.application.views.ordemServico;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
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
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.component.orderedlayout.FlexLayout.FlexWrap;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.component.Component;


@PageTitle("Ordem de Servico")
@Route(value = "my-view5", layout = MainLayout.class)
public class OrdemServicoView extends VerticalLayout {

    private DaoProduto produtoRepository;
    private DaoFuncionario funcionarioRepository;
    private DaoCliente clienteRepository;
    private DaoOrdemServico osRepository;
    private DaoOSProduto osProdutoRepository;
    private Grid<OrdemServico> grid = new Grid<>(OrdemServico.class, false);
    private ComboBox<StatusOS> status = new ComboBox<>("Status");
    private TextArea endereco = new TextArea("Endereço");
    private TextArea observacao = new TextArea("Observação");
    private DatePicker data = new DatePicker("Data Abertura");
    private DatePicker datap = new DatePicker("Data de Previsão");
    private ComboBox<Cliente> cliente = new ComboBox<>("Cliente");
    private ComboBox<Funcionario> funcionario = new ComboBox<>("Funcionario");
    private MultiSelectComboBox<Produto> produto = new MultiSelectComboBox<>("Produto");
    private TextField imagemPathField;
    private Button buttonTertiary = new Button();
    private Button buttonTertiary2 = new Button();
    private Button buttonTertiary3 = new Button();
    private Long osId;
    private TabSheet tabSheet;
    private String image;
    private List<String> imagePaths;
    private FlexLayout imageLayout;

    public OrdemServicoView() {
        try {
            produtoRepository = new DaoProduto();
            funcionarioRepository = new DaoFuncionario();
            clienteRepository = new DaoCliente();
            osRepository = new DaoOrdemServico();
            osProdutoRepository = new DaoOSProduto();

            tabSheet = new TabSheet();
            this.setWidth("100%");
            this.getStyle().set("flex-grow", "1");
            tabSheet.setWidth("100%");
            setTabSheetSampleData(tabSheet);

            this.add(tabSheet);
        } catch (SQLException e) {
            Notification.show("Erro ao inicializar o banco de dados: " + e.getMessage(), 3000,
                    Notification.Position.MIDDLE);
            e.printStackTrace();
        }
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
        status = new ComboBox<>("Status");
        endereco = new TextArea("Endereço");
        observacao = new TextArea("Observação");
        data = new DatePicker("Data Abertura");
        datap = new DatePicker("Data de Previsão");
        cliente = new ComboBox<>("Cliente");
        funcionario = new ComboBox<>("Funcionario");
        produto = new MultiSelectComboBox<>("Produto");
        status.setItems(StatusOS.values());
        setComboBoxClienteData(cliente);
        setComboBoxFuncionarioData(funcionario);
        setComboBoxProdutoData(produto);

        imagemPathField = new TextField("Caminhos das Imagens");
        imagemPathField.setWidthFull();
        imagemPathField.setReadOnly(true);
        imagemPathField.setWidth("200px");

        MemoryBuffer buffer = new MemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.setAcceptedFileTypes("image/jpeg", "image/png", "image/gif");
        upload.setMaxFiles(4);
        upload.setMaxFileSize(5 * 1024 * 1024);
        upload.setDropAllowed(true);

        List<String> imagePaths = new ArrayList<>();
        FlexLayout imageGallery = new FlexLayout();
        imageGallery.setFlexWrap(FlexWrap.WRAP);
        imageGallery.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        upload.addSucceededListener(event -> {
            if (imagePaths.size() >= 4) {
                Notification.show("Limite de 4 imagens atingido!", 3000, Notification.Position.MIDDLE);
                return;
            }

            try {
                String fileName = event.getFileName();
                String uploadDir = "C:/Users/sophi/Documents/";
                File targetFile = new File(uploadDir + fileName);

                try (InputStream inputStream = buffer.getInputStream()) {
                    Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
                imagePaths.add(targetFile.getAbsolutePath());

                imagemPathField.setValue(String.join(", ", imagePaths));

                addImageToGallery(imageGallery, targetFile.getAbsolutePath(), imagePaths);

                Notification.show("Imagem carregada com sucesso: " + fileName, 3000, Notification.Position.MIDDLE);
            } catch (IOException e) {
                e.printStackTrace();
                Notification.show("Falha ao fazer upload da imagem: " + e.getMessage(), 3000, Notification.Position.MIDDLE);
            }
        });

        Button saveButton = new Button("Salvar", event -> {
            if (cliente.isEmpty()) {
                Notification.show("Preencha o campo obrigatório: Cliente", 3000, Notification.Position.MIDDLE);
                return;
            }
            StatusOS statusOrdemServico = status.isEmpty() ? null : status.getValue();
            String enderecoOrdemServico = endereco.isEmpty() ? null : endereco.getValue();
            String observacaoOrdemServico = observacao.isEmpty() ? null : observacao.getValue();
            LocalDate dataAOrdemServico = data.isEmpty() ? null : data.getValue();
            LocalDate dataPOrdemServico = datap.isEmpty() ? null : datap.getValue();
            Cliente clienteOrdemServico = cliente.isEmpty() ? null : cliente.getValue();
            Funcionario funcionarioOrdemServico = funcionario.isEmpty() ? null : funcionario.getValue();
            Set<Produto> produtosSelecionados = produto.getValue();

            OrdemServico os = new OrdemServico(statusOrdemServico, clienteOrdemServico, enderecoOrdemServico,
                    funcionarioOrdemServico, dataAOrdemServico, dataPOrdemServico, observacaoOrdemServico);
            os.setId(osId);

            os.setImagens(imagePaths);

            List<Produto> produtosSelecionadosList = new ArrayList<>(produtosSelecionados);

            boolean sucesso;
            try {
                if (osId != null && osId > 0) {
                    sucesso = osRepository.updateOrdemServico(os, produtosSelecionadosList);
                    if (sucesso) {
                        Notification.show("OS atualizada com sucesso!");
                    } else {
                        Notification.show("Erro ao atualizar a OS", 3000, Notification.Position.MIDDLE);
                    }
                } else {
                    long idOs = osRepository.saveOrdemServico(os);
                    sucesso = idOs > 0;
                    if (sucesso) {
                        Notification.show("OS salva com sucesso!");
                    } else {
                        Notification.show("Erro ao salvar a OS", 3000, Notification.Position.MIDDLE);
                    }
                }
        
                if (sucesso) {
                    clearForm();
                    tabSheet.setSelectedIndex(0);
                    refreshGrid();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Notification.show("Erro ao processar a operação", 3000, Notification.Position.MIDDLE);
            }
            
        });

        // Configurações de estilo e layout
        status.addClassName("rounded-text-field");
        endereco.addClassName("rounded-text-field");
        observacao.addClassName("rounded-text-field");
        data.addClassName("rounded-text-field");
        datap.addClassName("rounded-text-field");
        cliente.addClassName("rounded-text-field");
        cliente.setRequiredIndicatorVisible(true);
        funcionario.addClassName("rounded-text-field");
        produto.addClassName("rounded-text-field");
        layout2.getStyle().set("border-radius", "15px");
        layout2.getStyle().set("border", "1px solid #ccc");
        layout2.getStyle().set("box-shadow", "0 0 2px rgba(0 , 0, 0, 0.2)");
        layout2.setWidth("1100px");
        layout2.getStyle().set("margin", "0 auto");
        layout3.setAlignItems(FlexComponent.Alignment.END);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.getStyle().set("border-radius", "25px");

        formLayout3Col.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 3));

        formLayout2Col.add(produto, status, endereco, observacao, funcionario, upload);
        formLayout3Col.add(cliente, data, datap);
        layout2.add(formLayout3Col, formLayout2Col, imageGallery, space);
        layout3.add(saveButton);
        layout.add(layout2, layout3);
        addOSContentDiv.add(space1, layout);

        return addOSContentDiv;
    }

    //Grid content
    private Grid<OrdemServico> createGrid() {
        grid = new Grid<>(OrdemServico.class, false);
        grid.addClassName("borderless-grid");
        grid.setAllRowsVisible(true);

        grid.addColumn(ordemServico -> ordemServico.getCliente() != null ? ordemServico.getCliente().getNome() : "N/A").setHeader("Cliente").setSortable(true);
        grid.addColumn(ordemServico -> ordemServico.getStatusOS() != null ? ordemServico.getStatusOS().name() : "N/A").setHeader("Status").setSortable(true);

        grid.addColumn(OrdemServico::getDataAbertura).setHeader("Data Abertura");
        grid.addColumn(OrdemServico::getDataPrevFinaliza).setHeader("Data de Previsão");

        grid.addComponentColumn(ordemServico -> {
            Button detalhes = new Button("Ver Detalhes", e -> {
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Detalhes da OS " + ordemServico.getId());

                VerticalLayout content = new VerticalLayout();
                content.setPadding(true);
                content.setSpacing(true);

                if (ordemServico.getImagens() != null && !ordemServico.getImagens().isEmpty()) {
                    HorizontalLayout imageLayout = new HorizontalLayout();
                    for (String imagePath : ordemServico.getImagens()) {
                        Image image = new Image(imagePath, "Imagem da OS");
                        image.setWidth("100px");
                        image.setHeight("100px");
                        imageLayout.add(image);
                    }
                    content.add(new Text("Imagens:"));
                    content.add(imageLayout);
                } else {
                    content.add(new Text("Nenhuma imagem disponível."));
                }

                // Adicionar a lista de produtos
                List<ProdutoOS> produtosOS = osProdutoRepository.getProdutoOSsByOrdemServicoId(ordemServico.getId());
                if (produtosOS != null && !produtosOS.isEmpty()) {
                    content.add(new Text("Produtos:"));
                    produtosOS.forEach(produtoOS -> {
                        Produto produto = produtoOS.getProduto();
                        content.add(new Text("- " + produto.getNome()));
                    });
                } else {
                    content.add(new Text("Nenhum produto associado a esta OS."));
                }
        
                content.add(new Text("Funcionário: " + (ordemServico.getFuncionario() != null ? ordemServico.getFuncionario() : "N/A")));
                content.add(new Text("Endereço: " + (ordemServico.getEndereco() != null ? ordemServico.getEndereco() : "N/A")));
                content.add(new Text("Observação: " + (ordemServico.getObservacao() != null ? ordemServico.getObservacao() : "N/A")));

                Button closeButton = new Button("Fechar", event -> dialog.close());
                content.add(closeButton);

                dialog.add(content);
                dialog.open();
            });

            detalhes.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            return detalhes;
        }).setHeader("Detalhes");

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

        grid.addItemDoubleClickListener(event -> {
            OrdemServico os = event.getItem();
            editOrdemServico(os);
            tabSheet.setSelectedIndex(1);
        });

        grid.setItems(osRepository.getAllOrdensServico());

        return grid;
    }

    private void addImageToGallery(FlexLayout imageGallery, String imagePath, List<String> imagePaths) {
        VerticalLayout imageContainer = new VerticalLayout();
        imageContainer.setSpacing(false);
        imageContainer.setPadding(false);
        imageContainer.setAlignItems(FlexComponent.Alignment.CENTER);
    
        Image image = new Image("file:///" + imagePath, "Uploaded Image");
        image.setWidth("150px");
        image.setHeight("150px");
        image.getStyle().set("object-fit", "cover");

        Button deleteButton = new Button("Excluir", VaadinIcon.TRASH.create(), event -> {
            imagePaths.remove(imagePath);
            imageGallery.remove(imageContainer);
            imagemPathField.setValue(String.join(", ", imagePaths));
            Notification.show("Imagem removida com sucesso!", 3000, Notification.Position.MIDDLE);
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
    
        imageContainer.add(image, deleteButton);
        imageGallery.add(imageContainer);
    }

    private void editOrdemServico(OrdemServico os) {
        osId = os.getId();
        status.setValue(os.getStatusOS());
        endereco.setValue(String.valueOf(os.getEndereco()));
        observacao.setValue(String.valueOf(os.getObservacao()));
        funcionario.setValue(os.getFuncionario());
        cliente.setValue(os.getCliente());
        data.setValue(os.getDataAbertura());
        datap.setValue(os.getDataPrevFinaliza());
    

        List<ProdutoOS> produtosSelecionados = osProdutoRepository.getProdutoOSsByOrdemServicoId(osId);
        Set<Produto> produtos = produtosSelecionados.stream()
                .map(ProdutoOS::getProduto)
                .collect(Collectors.toSet());
        produto.setValue(produtos);
    
        imagePaths.clear();
        imageLayout.removeAll();
    
        List<String> imagens = os.getImagens();
        if (imagens != null && !imagens.isEmpty()) {
            for (String imagePath : imagens) {
                imagePaths.add(imagePath);
                imageLayout.add(createImageComponent(imagePath));
            }
            imagemPathField.setValue(String.join(", ", imagens));
            Notification.show("Imagens carregadas", 3000, Notification.Position.MIDDLE);
        } else {
            imagemPathField.clear();
            Notification.show("Nenhuma imagem associada.", 3000, Notification.Position.MIDDLE);
        }
    }

    private Component createImageComponent(String imagePath) {
        VerticalLayout imageContainer = new VerticalLayout();
        imageContainer.setSpacing(false);
        imageContainer.setPadding(false);
        imageContainer.setAlignItems(FlexComponent.Alignment.CENTER);
    
        // Exibe a imagem
        Image image = new Image("file:///" + imagePath, "Uploaded Image");
        image.setWidth("150px");
        image.setHeight("150px");
        image.getStyle().set("object-fit", "cover");
    
        // Botão para excluir a imagem
        Button deleteButton = new Button("Excluir", VaadinIcon.TRASH.create(), event -> {
            imagePaths.remove(imagePath); // Remove da lista global
            imageLayout.remove(imageContainer); // Remove do layout visual
            imagemPathField.setValue(String.join(", ", imagePaths)); // Atualiza o campo com os caminhos
            Notification.show("Imagem removida com sucesso!", 3000, Notification.Position.MIDDLE);
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
    
        // Adiciona imagem e botão ao container
        imageContainer.add(image, deleteButton);
        return imageContainer;
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

    private void clearForm() {
        osId = null;
        status.clear();
        endereco.clear();
        observacao.clear();
        cliente.clear();
        funcionario.clear();
        data.clear();
        datap.clear();
        produto.clear();
        image = null;
    }
}
