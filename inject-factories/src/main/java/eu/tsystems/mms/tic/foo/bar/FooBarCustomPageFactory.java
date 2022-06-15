package eu.tsystems.mms.tic.foo.bar;

import com.google.inject.AbstractModule;
import eu.tsystems.mms.tic.testframework.pageobjects.internal.PageFactory;
import factories.MyPageFactory;

/**
 * Created on 09.05.2022
 *
 * @author mgn
 */
public class FooBarCustomPageFactory extends AbstractModule {

    @Override
    protected void configure() {
        bind(PageFactory.class).to(MyPageFactory.class);
    }

}
