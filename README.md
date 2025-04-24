# MiniBlockChain Project

## Artifact Identification

### Project Title
MiniBlockChain: A Simplified Blockchain Implementation

### Abstract
The MiniBlockChain project is a simplified yet fully functional implementation of blockchain technology, designed as an educational tool to demonstrate core blockchain principles. The system implements essential blockchain features including transaction processing, mining with proof-of-work, and blockchain verification using Merkle trees.

Key contributions include:
- Implementation of secure transaction handling using RSA encryption and digital signatures
- A proof-of-work mining system with configurable difficulty
- Merkle tree implementation for efficient transaction verification
- Comprehensive testing framework demonstrating tamper detection
- Interactive command-line interface for blockchain interaction

The artifact directly supports the reproducibility of all experiments described in the report, including transaction validation, mining operations, and security testing.

## Artifact Dependencies and Requirements

### Hardware Requirements
- Processor: 2+ cores recommended for mining operations
- RAM: Minimum 4GB
- Storage: 100MB free space

### Operating System Requirements
- Any OS with Java support (Windows/Linux/MacOS)
- Tested on Windows 10 and above

### Software Dependencies
- Java Development Kit (JDK) 8 or higher
- Java Runtime Environment (JRE) 8 or higher
- No external libraries required (uses only Java standard libraries)

### Required Java Libraries
- java.security: For cryptographic operations
- javax.crypto: For RSA encryption/decryption
- java.util: For data structures
- java.math: For BigInteger operations in proof-of-work
- java.time: For timestamp generation

### Input Data
- No external dataset required
- System generates test data through the command-line interface
- Initial wallet balance set to 100,000 units for new users

## Installation and Deployment Process

### Installation Steps
1. Download the code project from moodle:

2. Compile the Java files in IntelliJ:

3. Run the application:

## Reproducibility of Experiments

### Experiment Workflow
1. Basic Transaction Flow (15-20 minutes):
   - Create two users (Alice and Bob)
   - Create a transaction from Alice to Bob
   - Mine a block containing the transaction
   - Verify the transaction and block integrity

2. Security Testing (20-30 minutes):
   - Create and verify multiple transactions
   - Attempt transaction tampering through the malicious actions menu
   - Verify detection of tampered transactions
   - Test blockchain integrity verification

3. Performance Testing (30-40 minutes):
   - Create multiple users and transactions
   - Test mining with different difficulty levels
   - Measure transaction processing time
   - Verify Merkle tree functionality

### Expected Results
The experiments should demonstrate:
1. Successful creation and verification of transactions
2. Detection of tampered transactions and blocks
3. Proper functioning of the proof-of-work system
4. Effective Merkle tree verification
5. System resilience against various attack vectors

All results can be validated through the command-line interface using the verification options (Option 10 in the menu).

### Correlation with Report Results
The experimental workflow directly corresponds to the results section of the report:
- Transaction validation results match Section 5.3.3
- Mining performance aligns with Section 6.1
- Security test results correspond to Section 6.1
- All test scenarios described in Section 5.4 can be reproduced

### Command-Line Interface Options
1. View blockchain
2. Create user
3. List all users
4. Create transaction
5. View mempool
6. Mine new block
7. View block details
8. View transaction details
9. Perform malicious actions
10. Verify Blockchain
11. Exit

Each option provides interactive prompts for required parameters and displays clear results for verification.