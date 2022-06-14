package io.defitrack.protocol.kyberswap.pooling

import io.defitrack.pool.StandardLpPositionProvider
import io.defitrack.token.ERC20Resource
import org.springframework.stereotype.Service

@Service
class KyberswapEthereumPoolingPositionProvider(
    kyberswapEthereumPoolingMarketProvider: KyberswapEthereumPoolingMarketProvider,
    erC20Resource: ERC20Resource
) : StandardLpPositionProvider(kyberswapEthereumPoolingMarketProvider, erC20Resource)