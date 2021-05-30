import socket

if __name__ == '__main__':
    # socket.setblocking(0)
    # hostname = socket.gethostname()
    # UDP_IP = socket.gethostbyname(hostname)

    # the ip address of the server
    # UDP_IP = '192.168.0.106'
    UDP_IP = '10.9.9.118'

    # tge socket port number
    UDP_PORT = 4569

    # initialize the socket and bind it
    sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
    sock.setblocking(0)
    sock.bind((UDP_IP, UDP_PORT))\

    # receive data from watch
    while True:
        print("Data: ")
        data, addr = sock.recvfrom(1024)
        print(data)