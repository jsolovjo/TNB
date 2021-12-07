package org.jboss.fuse.tnb.sns.service;

import org.jboss.fuse.tnb.aws.service.AWSService;
import org.jboss.fuse.tnb.common.account.Accounts;
import org.jboss.fuse.tnb.common.service.ServiceFactory;
import org.jboss.fuse.tnb.sns.account.SNSAccount;
import org.jboss.fuse.tnb.sns.validation.SNSValidation;
import org.jboss.fuse.tnb.sqs.service.SQS;
import org.jboss.fuse.tnb.sqs.validation.SQSValidation;

import org.junit.jupiter.api.extension.ExtensionContext;

import com.google.auto.service.AutoService;

import software.amazon.awssdk.services.sns.SnsClient;

@AutoService(SNS.class)
public class SNS extends AWSService<SNSAccount, SnsClient, SNSValidation> {
    private final SQS sqs = ServiceFactory.create(SQS.class);

    @Override
    public SNSAccount account() {
        if (account == null) {
            LOG.debug("Creating new SNS account");
            account = Accounts.get(SNSAccount.class);
            // There two are derived other values
            account.setTopicUrlPrefix(String.format("https://sns.%s.amazonaws.com/%s/", account.region(), account.accountId()));
            account.setTopicArnPrefix(String.format("arn:aws:sns:%s:%s:", account.region(), account.accountId()));
        }
        return account;
    }

    public SQSValidation getSQSValidation() {
        return sqs.validation();
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        super.afterAll(extensionContext);
        sqs.afterAll(extensionContext);
    }

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        sqs.beforeAll(extensionContext);
        LOG.debug("Creating new SNS validation");
        validation = new SNSValidation(client(SnsClient.class), account(), sqs);
    }
}
