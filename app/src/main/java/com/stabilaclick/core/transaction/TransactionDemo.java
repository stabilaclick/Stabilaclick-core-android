package com.stabilaclick.core.transaction;

import com.stabilaclick.core.common.StringStabilaUtil;
import com.stabilaclick.core.common.common.utils.TransactionUtils;
import com.stabilaclick.core.common.common.utils.abi.CancelException;
import com.stabilaclick.core.common.common.utils.abi.EncodingException;
import com.stabilaclick.core.common.net.CipherException;
import com.stabilaclick.core.common.net.StabilaAPI;
import com.stabilaclick.core.wallet.Wallet;

import org.stabila.api.GrpcAPI;
import org.stabila.protos.Contract;
import org.stabila.protos.Protocol;

import java.io.IOException;

public class TransactionDemo {
    public void send() {
        byte[] ToRaw;
        String toAddress = "";
        double count = 1;
        ToRaw = StringStabilaUtil.decodeFromBase58Check(toAddress);

        Wallet wallet = new Wallet();
        // trx
        Contract.TransferContract contract = StabilaAPI.createTransferContract(ToRaw, StringStabilaUtil.decodeFromBase58Check(wallet.getAddress()), (long) (count * 1000000.0d));
        Protocol.Transaction transactionTRX = StabilaAPI.createTransaction4Transfer(contract);

        //trx10

        String tokenId = "";
        GrpcAPI.TransactionExtention transferAssetTransaction = StabilaAPI.createTransferAssetTransaction(ToRaw, tokenId.getBytes(), StringStabilaUtil.decodeFromBase58Check(wallet.getAddress()), (long) count);
        if (transferAssetTransaction.hasResult()) {
            Protocol.Transaction transactionTRX10 = transferAssetTransaction.getTransaction();
        }

        //trx20
        String contractAddresss = "";

        String[] parameters = new String[]{contractAddresss,
                "transfer(address,uint256)", toAddress, "false", "100000000", "0"};
        GrpcAPI.TransactionExtention transactionExtention = null;
        try {
            transactionExtention = StabilaAPI.triggerContract(parameters, StringStabilaUtil.decodeFromBase58Check(wallet.getAddress()));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CipherException e) {
            e.printStackTrace();
        } catch (CancelException e) {
            e.printStackTrace();
        } catch (EncodingException e) {
            e.printStackTrace();
        }
        if (transactionExtention.hasResult()) {
            Protocol.Transaction transactionTRX20 = transactionExtention.getTransaction();
        }

        //sign
        Protocol.Transaction mTransactionSigned = TransactionUtils.setTimestamp(transactionTRX);
        mTransactionSigned = TransactionUtils.sign(mTransactionSigned, wallet.getECKey());


        //broadcastTransaction
        boolean sent = StabilaAPI.broadcastTransaction(mTransactionSigned);


    }
}
