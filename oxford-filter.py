import boto3
import json
import time
import logging
import sys

def read_words(filename):
    """Read words from oxford-words.txt file"""
    with open(filename, 'r', encoding='utf-8') as f:
        words = [line.strip() for line in f if line.strip()]
    return words

def read_prompt(filename):
    """Read the base prompt from prompt4.txt"""
    with open(filename, 'r', encoding='utf-8') as f:
        return f.read()

def create_batches(words, batch_size=500, overlap=400):
    """Create overlapping batches with gradual build-up pattern"""
    batches = []
    n = len(words)
    step = batch_size - overlap
    
    # Build-up phase: gradually increase batch size
    for size in range(step, batch_size, step):
        if size <= n:
            batches.append(words[:size])
    
    # Full-size sliding phase
    start = 0
    while start + batch_size <= n:
        batches.append(words[start:start + batch_size])
        start += step
    
    # Wind-down phase: remaining elements
    while start < n:
        batches.append(words[start:n])
        start += step
    
    return batches

def send_to_claude(prompt, region='us-east-1'):
    """Send prompt to Claude 3.5 Sonnet v2 and return response"""
    client = boto3.client('bedrock-runtime', region_name=region)
    
    try:
        body = json.dumps({
            "anthropic_version": "bedrock-2023-05-31",
            "max_tokens": 4000,
            "temperature": 0.1,
            "messages": [
                {
                    "role": "user",
                    "content": prompt
                }
            ]
        })
        
        response = client.invoke_model(
            modelId='us.anthropic.claude-3-5-sonnet-20241022-v2:0',
            body=body,
            contentType='application/json'
        )
        
        result = json.loads(response['body'].read().decode('utf-8'))
        return result['content'][0]['text']
    
    except Exception as e:
        return f"Error: {str(e)}"

def setup_logging():
    """Setup logging to both console and file"""
    logging.basicConfig(
        level=logging.INFO,
        format='%(asctime)s - %(message)s',
        handlers=[
            logging.FileHandler('oxford-filter.log'),
            logging.StreamHandler(sys.stdout)
        ]
    )
    return logging.getLogger(__name__)

def main():
    logger = setup_logging()
    
    # Read input files
    words = read_words('oxford-words.txt')
    base_prompt = read_prompt('prompt4.txt')
    
    logger.info(f"Total words: {len(words)}")
    
    # Create overlapping batches
    batches = create_batches(words)
    logger.info(f"Created {len(batches)} batches")
    
    # Log last batch info
    last_batch = batches[-1]
    last_5_words = last_batch[-5:] if len(last_batch) >= 5 else last_batch
    logger.info(f"Last batch - size: {len(last_batch)}, last 5 words: {', '.join(last_5_words)}")
    
    # Process each batch
    for i, batch in enumerate(batches, 1):
        logger.info(f"\nProcessing batch {i}/{len(batches)} ({len(batch)} words)")
        logger.info(f"Words in batch: {', '.join(batch)}")
        
        # Create prompt with current batch
        words_text = '\n'.join(batch)
        full_prompt = base_prompt + words_text
        
        # Send to Claude 3.5 Sonnet v2
        response = send_to_claude(full_prompt)
        
        logger.info(f"Response for batch {i}:")
        logger.info(response)
        logger.info("-" * 80)
        
        # Add delay to avoid rate limiting
        if i < len(batches):
            time.sleep(2)

if __name__ == "__main__":
    main()