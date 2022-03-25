#!/bin/bash

docker build -t defitrack/defitrack:arbitrum-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-arbitrum
docker build -t defitrack/defitrack:avalanche-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-avalanche
docker build -t defitrack/defitrack:bsc-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-bsc
docker build -t defitrack/defitrack:ethereum-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-ethereum
docker build -t defitrack/defitrack:fantom-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-fantom
docker build -t defitrack/defitrack:optimism-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-optimism
docker build -t defitrack/defitrack:polygon-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-polygon
docker build -t defitrack/defitrack:polygon-mumbai-${BRANCH_NAME} defitrack-rest/defitrack-blockchain-services/defitrack-polygon-mumbai

docker build -t defitrack/defitrack:uniswap-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-uniswap
docker build -t defitrack/defitrack:quickswap-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-quickswap
docker build -t defitrack/defitrack:sushiswap-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-sushiswap
docker build -t defitrack/defitrack:adamant-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-adamant
docker build -t defitrack/defitrack:api-gw-${BRANCH_NAME} defitrack-rest/defitrack-api-gw
docker build -t defitrack/defitrack:dfyn-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-dfyn
docker build -t defitrack/defitrack:dinoswap-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-dinoswap
docker build -t defitrack/defitrack:balancer-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-balancer
docker build -t defitrack/defitrack:dmm-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-dmm
docker build -t defitrack/defitrack:humandao-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-humandao
docker build -t defitrack/defitrack:hop-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-hop
docker build -t defitrack/defitrack:idex-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-idex
docker build -t defitrack/defitrack:mstable-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-mstable
docker build -t defitrack/defitrack:spirit-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-spirit
docker build -t defitrack/defitrack:spooky-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-spooky
docker build -t defitrack/defitrack:aave-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-aave
docker build -t defitrack/defitrack:beefy-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-beefy
docker build -t defitrack/defitrack:curve-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-curve
docker build -t defitrack/defitrack:polycat-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-polycat
docker build -t defitrack/defitrack:convex-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-convex
docker build -t defitrack/defitrack:yearn-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-yearn
docker build -t defitrack/defitrack:compound-${BRANCH_NAME} defitrack-rest/defitrack-protocol-services/defitrack-compound
docker build -t defitrack/defitrack:price-${BRANCH_NAME} defitrack-rest/defitrack-price
docker build -t defitrack/defitrack:balance-${BRANCH_NAME} defitrack-rest/defitrack-balance
docker build -t defitrack/defitrack:abi-${BRANCH_NAME} defitrack-rest/defitrack-abi
docker build -t defitrack/defitrack:erc20-${BRANCH_NAME} defitrack-rest/defitrack-erc20
