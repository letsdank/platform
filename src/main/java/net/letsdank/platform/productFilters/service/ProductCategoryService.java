package net.letsdank.platform.productFilters.service;

import lombok.AllArgsConstructor;
import net.letsdank.platform.productFilters.entity.ProductCategory;
import net.letsdank.platform.productFilters.repository.ProductCategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.List;

@Service
@AllArgsConstructor
public class ProductCategoryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductCategoryService.class);
    private final ProductCategoryRepository productCategoryRepository;

    public void upload(MultipartFile file) {
        productCategoryRepository.deleteAll();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(file.getInputStream());

            Element root = document.getDocumentElement();

            NodeList categoriesNodeList = root.getElementsByTagName("category");
            for (int i = 0; i < categoriesNodeList.getLength(); i++) {
                Element categoryElement = (Element) categoriesNodeList.item(i);
                long categoryId = Long.parseLong(categoryElement.getAttribute("id"));
                String categoryName = categoryElement.getAttribute("name");
                String categoryUniqueName = categoryElement.getAttribute("uniq_name");
                long parentId = Long.parseLong(categoryElement.getAttribute("parent-id"));
                String type = categoryElement.getAttribute("type");
                Long offersNum = Long.parseLong(categoryElement.getAttribute("offers-num"));
                Long modelsNum = Long.parseLong(categoryElement.getAttribute("models-num"));
                boolean visual = Boolean.parseBoolean(categoryElement.getAttribute("visual"));

                productCategoryRepository.save(new ProductCategory(categoryId, categoryName, categoryUniqueName, parentId, type, offersNum, modelsNum, visual, List.of()));

                NodeList filtersNodeList = categoryElement.getElementsByTagName("filter");
                for (int j = 0; j < filtersNodeList.getLength(); j++) {

                }
            }

        } catch (Exception e){
            LOGGER.error("Error while parsing XML file: " + e.getMessage());
        }

    }
}
