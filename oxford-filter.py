import boto3
import json
import time

def read_words(filename):
    """Read words from oxford-words.txt file"""
    with open(filename, 'r', encoding='utf-8') as f:
        words = [line.strip() for line in f if line.strip()]
    return words

def read_prompt(filename):
    """Read the base prompt from prompt4.txt"""
    with open(filename, 'r', encoding='utf-8') as f:
        return f.read()

def create_batches(words, batch_size=500, overlap=250):
    """Create overlapping batches of words with extra first and last batches"""
    batches = []
    
    # First batch: 1 to 250
    if len(words) > 0:
        batches.append(words[:250])
    
    # Regular batches starting from 1 to 500, then 251 to 750, etc.
    start = 0
    while start < len(words):
        end = min(start + batch_size, len(words))
        batch = words[start:end]
        batches.append(batch)
        
        if end >= len(words):
            break
            
        start += (batch_size - overlap)
    
    # Extra last batch: last (batch_size - overlap) words
    if len(words) > 250:
        batches.append(words[-(batch_size - overlap):])
    
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

def main():
    # Read input files
    words = read_words('oxford-words.txt')
    base_prompt = read_prompt('prompt4.txt')
    
    print(f"Total words: {len(words)}")
    
    # Create overlapping batches
    batches = create_batches(words)
    print(f"Created {len(batches)} batches")
    
    # Log last batch info
    last_batch = batches[-1]
    last_5_words = last_batch[-5:] if len(last_batch) >= 5 else last_batch
    print(f"Last batch - size: {len(last_batch)}, last 5 words: {', '.join(last_5_words)}")
    
    # Process each batch
    for i, batch in enumerate(batches, 1):
        print(f"\nProcessing batch {i}/{len(batches)} ({len(batch)} words)")
        
        # Create prompt with current batch
        words_text = '\n'.join(batch)
        full_prompt = base_prompt + words_text
        
        # Send to Claude 3.5 Sonnet v2
        response = send_to_claude(full_prompt)
        
        print(f"Response for batch {i}:")
        print(response)
        print("-" * 80)
        
        # Add delay to avoid rate limiting
        if i < len(batches):
            time.sleep(2)

if __name__ == "__main__":
    main()