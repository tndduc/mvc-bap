package bap.jp.mvcbap.batch.bai2;


import bap.jp.mvcbap.entity.Category;
import bap.jp.mvcbap.entity.Product;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class ProductFieldSetMapper implements FieldSetMapper<Product> {
    @Override
    public Product mapFieldSet(FieldSet fieldSet) throws BindException {
	Product product = new Product();
	product.setProductName(fieldSet.readString("productName"));
	product.setPrice(fieldSet.readBigDecimal("price"));

	if (fieldSet.readString("categoryId") != null && !fieldSet.readString("categoryId").isEmpty()) {
	    Category category = new Category();
	    category.setId(fieldSet.readInt("categoryId"));
	    product.setCategory(category);
	}
	product.setDeleteFlg(fieldSet.readBoolean("deleteFlg"));
	return product;
    }
}