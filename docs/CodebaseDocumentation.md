# MiniBlockChain Codebase Documentation

## Overview
The MiniBlockChain project is a simple implementation of a blockchain system. It includes classes for managing blocks, transactions, mining, and the overall blockchain structure. Below is a detailed documentation of each component in the codebase.

## Architectural Overview
The system follows a layered architecture with these key components:

1. **Core Blockchain Layer**:
   - Block: Fundamental building blocks of the chain
   - BlockChain: Manages block validation and chaining
   - MerkleTree: Provides transaction verification structure

2. **Transaction Layer**:
   - Transaction: Signed financial transfers
   - MemPool: Transaction queue before mining
   - Verifier: Validates transaction integrity

3. **User Layer**:
   - User: Account with wallet and keys
   - UserManager: Central user registry

4. **Mining Layer**:
   - Miner: Creates new blocks through PoW
   - MiningTest: Validates mining operations

5. **Interface Layer**:
   - CommandLineInterface: User interaction portal
   - Main: System entry point

## Classes

[Existing Classes documentation remains unchanged...]

## Security Features

### Cryptographic Protections
- **Transaction Signing**: RSA-2048 signatures verify transaction authenticity
- **Block Hashing**: SHA-256 hashes ensure block integrity
- **Merkle Roots**: Detect transaction tampering in blocks
- **Proof-of-Work**: Mining difficulty prevents spam

### Validation Mechanisms
- Block header verification
- Transaction signature checks
- Merkle root validation
- Chain continuity enforcement
- Comprehensive test cases for tamper detection

## Key Process Flows

### Transaction Lifecycle
1. User creates signed transaction
2. Transaction enters MemPool queue
3. Miner collects transactions for new block
4. Block undergoes PoW validation
5. Valid block added to blockchain
6. Transactions removed from MemPool

### Block Validation Flow
1. Verify block hash matches content
2. Check previous block reference
3. Validate all transaction signatures
4. Confirm Merkle root matches transactions
5. Verify proof-of-work difficulty

## Component Interactions
![Architecture Diagram](images/5_4_1.PNG)

[Rest of existing documentation remains unchanged...]
